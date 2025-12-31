package org.example.inventory.service.impl;

import jakarta.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.inventory.dto.OrderDTO;
import org.example.inventory.entity.Inventory;
import org.example.inventory.exception.InsufficientStockException;
import org.example.inventory.repository.InventoryRepository;
import org.example.inventory.service.InventoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

  private static final Integer DEFAULT_QUANTITY = 0;

  private final InventoryRepository inventoryRepository;

  @Override
  public List<Inventory> getAll() {
    return inventoryRepository.findAll();
  }

  @Override
  @Transactional
  public Inventory updateQuantity(Long id, Integer quantity) {
    Inventory inventory = findByIdOrThrow(id);

    inventory.setQuantity(quantity);

    log.info("Updated quantity for inventory id: {} to {}", id, quantity);
    return inventory;
  }

  @Override
  @Transactional
  public void adjustInventory(OrderDTO orderDTO) {
    Long productId = orderDTO.productId();
    Integer requestedQuantity = orderDTO.quantity();
    Inventory inventory = findByProductIdOrThrow(productId);

    if (inventory.getQuantity() < requestedQuantity) {
      throw new InsufficientStockException(
          String.format(
              "Not enough stock for productId: %s. Available: %d, Requested: %d",
              productId, inventory.getQuantity(), requestedQuantity));
    }

    inventory.setQuantity(inventory.getQuantity() - requestedQuantity);

    log.info(
        "Adjusted inventory for productId: {}. New quantity: {}",
        productId,
        inventory.getQuantity());
  }

  @Override
  @Transactional
  public void createInventory(Long productId, String productName, BigDecimal productPrice) {
    if (inventoryRepository.findByProductId(productId).isPresent()) {
      log.warn("Inventory for productId={} already exists. Skipping creation.", productId);
      return;
    }

    Inventory inventory =
        Inventory.builder()
            .productId(productId)
            .productName(productName)
            .productPrice(productPrice)
            .quantity(DEFAULT_QUANTITY)
            .build();

    inventoryRepository.save(inventory);

    log.info("Created inventory for productId={}", productId);
  }

  @Override
  @Transactional
  public void updateProductDetails(Long productId, String productName, BigDecimal productPrice) {
    Inventory inventory = findByProductIdOrThrow(productId);

    inventory.setProductName(productName);
    inventory.setProductPrice(productPrice);

    log.info("Updated product details for productId={}", productId);
  }

  @Override
  @Transactional
  public void deleteByProductId(Long productId) {
    Inventory inventory = findByProductIdOrThrow(productId);

    inventoryRepository.delete(inventory);

    log.info("Deleted inventory for productId={}", productId);
  }

  private Inventory findByIdOrThrow(Long id) {
    return inventoryRepository
        .findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Inventory not found with id: " + id));
  }

  private Inventory findByProductIdOrThrow(Long productId) {
    return inventoryRepository
        .findByProductId(productId)
        .orElseThrow(
            () -> new EntityNotFoundException("Inventory not found for productId: " + productId));
  }
}
