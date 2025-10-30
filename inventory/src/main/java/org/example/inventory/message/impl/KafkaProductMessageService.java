package org.example.inventory.message.impl;

import jakarta.transaction.Transactional;
import java.util.Optional;
import org.example.inventory.entity.Inventory;
import org.example.inventory.message.ProductMessageService;
import org.example.inventory.repository.InventoryRepository;
import org.example.message.ActionType;
import org.example.message.Product;
import org.example.message.ProductEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaProductMessageService implements ProductMessageService {

  private final InventoryRepository inventoryRepository;

  public KafkaProductMessageService(final InventoryRepository inventoryRepository) {
    this.inventoryRepository = inventoryRepository;
  }

  @Override
  @Transactional
  @KafkaListener(topics = "product-events", groupId = "inventory-consumer-group")
  public void productEvent(final ProductEvent productEvent) {
    final ActionType actionType = productEvent.getActionType();
    final Product product = productEvent.getProduct();

    switch (actionType) {
      case ADD -> add(product);
      case DELETE -> delete(product);
      case UPDATE -> update(product);
    }
  }

  private void add(final Product product) {
    final Long productId = product.getId();
    final Optional<Inventory> existedInventory = inventoryRepository.findByProductId(productId);

    if (existedInventory.isEmpty()) {
      final Inventory inventory =
          new Inventory(productId, product.getName(), product.getPrice(), 0);

      inventoryRepository.save(inventory);
    }
  }

  private void delete(final Product product) {
    final Long productId = product.getId();

    inventoryRepository.deleteByProductId(productId);
  }

  private void update(final Product product) {
    final Long productId = product.getId();
    final Optional<Inventory> existedInventory = inventoryRepository.findByProductId(productId);

    if (existedInventory.isPresent()) {
      final Inventory inventory = existedInventory.get();

      inventory.setProductName(product.getName());
      inventory.setProductPrice(product.getPrice());

      inventoryRepository.save(inventory);
    }
  }
}
