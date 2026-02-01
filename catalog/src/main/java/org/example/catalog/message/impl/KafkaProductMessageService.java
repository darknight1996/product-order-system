package org.example.catalog.message.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.catalog.entity.Product;
import org.example.catalog.message.ProductMessageService;
import org.example.message.ActionType;
import org.example.message.ProductEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaProductMessageService implements ProductMessageService {

  private final KafkaTemplate<String, ProductEvent> kafkaTemplate;

  @Value("${app.kafka.topics.product-events:product-events}")
  private String topicName;

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
    ProductEvent event = createProductEvent(product, actionType);

    String messageKey = product.getId().toString();

    sendMessage(messageKey, event);
  }

  private ProductEvent createProductEvent(Product product, ActionType actionType) {
    return new ProductEvent(
        new org.example.message.Product(product.getId(), product.getName(), product.getPrice()),
        actionType);
  }

  private void sendMessage(String key, ProductEvent event) {
    log.info("Sending event {} for id: {}", event.actionType(), key);

    kafkaTemplate
        .send(topicName, key, event)
        .whenComplete((result, exception) -> handleSendResult(key, result, exception));
  }

  private void handleSendResult(
      String key, SendResult<String, ProductEvent> result, Throwable exception) {
    if (exception != null) {
      log.error("Failed to send message for id: {}", key, exception);
    } else {
      log.debug(
          "Message sent successfully for id: {}, offset: {}",
          key,
          result.getRecordMetadata().offset());
    }
  }
}
