package org.example.catalog.message.impl;

import org.example.catalog.entity.Product;
import org.example.catalog.message.ProductMessageService;
import org.example.message.ActionType;
import org.example.message.ProductEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProductMessageService implements ProductMessageService {

  private final KafkaTemplate<String, ProductEvent> kafkaTemplate;

  public KafkaProductMessageService(KafkaTemplate<String, ProductEvent> kafkaTemplate) {
    this.kafkaTemplate = kafkaTemplate;
  }

  @Override
  public void sendAdd(Product product) {
    sendEvent(product, ActionType.ADD);
  }

  @Override
  public void sendDelete(Product product) {
    sendEvent(product, ActionType.DELETE);
  }

  @Override
  public void sendUpdate(Product product) {
    sendEvent(product, ActionType.UPDATE);
  }

  private void sendEvent(Product product, ActionType actionType) {
    ProductEvent productEvent = createProductEvent(product, actionType);

    sendMessage(productEvent);
  }

  private ProductEvent createProductEvent(Product product, ActionType actionType) {
    return new ProductEvent(product.getId(), product.getName(), product.getPrice(), actionType);
  }

  private void sendMessage(ProductEvent productEvent) {
    kafkaTemplate.send("product-events", productEvent);
  }
}
