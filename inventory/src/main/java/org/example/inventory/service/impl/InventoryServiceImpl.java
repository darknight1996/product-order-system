package org.example.inventory.service.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import java.util.List;
import org.example.inventory.dto.OrderDTO;
import org.example.inventory.entity.Inventory;
import org.example.inventory.repository.InventoryRepository;
import org.example.inventory.service.InventoryService;
import org.springframework.stereotype.Service;

@Service
public class InventoryServiceImpl implements InventoryService {

  private final InventoryRepository inventoryRepository;

  public InventoryServiceImpl(InventoryRepository inventoryRepository) {
    this.inventoryRepository = inventoryRepository;
  }

  @Override
  public List<Inventory> getAll() {
    return inventoryRepository.findAll();
  }

  @Override
  public Inventory getById(Long id) {
    return getExistedInventory(id);
  }

  @Override
  public Inventory add(Inventory inventory) {
    return inventoryRepository.save(inventory);
  }

  @Override
  public void delete(Long id) {
    final Inventory existedInventory = getExistedInventory(id);

    inventoryRepository.delete(existedInventory);
  }

  @Override
  public Inventory updateQuantity(Long id, Integer quantity) {
    Inventory existedInventory = getExistedInventory(id);

    existedInventory.setQuantity(quantity);

    return inventoryRepository.save(existedInventory);
  }

  @Override
  @Transactional
  public boolean adjustInventory(OrderDTO orderDTO) {
    Long orderProductId = orderDTO.getProductId();
    Integer orderQuantity = orderDTO.getQuantity();

    Inventory inventory = getExistedInventoryByProductId(orderProductId);
    Integer inventoryQuantity = inventory.getQuantity();

    if (inventoryQuantity < orderQuantity) {
      return false;
    }

    inventory.setQuantity(inventoryQuantity - orderQuantity);
    inventoryRepository.save(inventory);

    return true;
  }

  private Inventory getExistedInventory(Long id) {
    return inventoryRepository
        .findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Inventory not found"));
  }

  private Inventory getExistedInventoryByProductId(Long productId) {
    return inventoryRepository
        .findByProductId(productId)
        .orElseThrow(() -> new EntityNotFoundException("Inventory not found"));
  }
}
