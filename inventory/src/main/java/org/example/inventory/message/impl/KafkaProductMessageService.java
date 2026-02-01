package org.example.inventory.message.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.inventory.message.ProductMessageService;
import org.example.inventory.service.InventoryService;
import org.example.message.ActionType;
import org.example.message.Product;
import org.example.message.ProductEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaProductMessageService implements ProductMessageService {

  private final InventoryService inventoryService;

  @Override
  @Transactional
  @KafkaListener(
      topics = "${app.kafka.topics.product-events}",
      groupId = "${spring.kafka.consumer.group-id}")
  public void productEvent(ProductEvent productEvent) {
    ActionType actionType = productEvent.actionType();
    Product product = productEvent.product();

    log.info("Received ProductEvent: action={}, productId={}", actionType, product.id());

    switch (actionType) {
      case ADD -> add(product);
      case DELETE -> delete(product);
      case UPDATE -> update(product);

      default -> log.warn("Received unknown action type: {}", actionType);
    }
  }

  private void add(Product product) {
    inventoryService.createInventory(product.id(), product.name(), product.price());

    log.info("Created new Inventory for productId={}", product.id());
  }

  private void update(Product product) {
    inventoryService.updateProductDetails(product.id(), product.name(), product.price());

    log.info("Updated Inventory for productId={}", product.id());
  }

  private void delete(Product product) {
    inventoryService.deleteByProductId(product.id());

    log.info("Deleted Inventory for productId={}", product.id());
  }
}
