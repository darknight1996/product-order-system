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

    public KafkaProductMessageService(final KafkaTemplate<String, ProductEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void sendAdd(final Product product) {
        final ProductEvent productEvent = createProductEvent(product, ActionType.ADD);

        sendMessage(productEvent);
    }

    @Override
    public void sendDelete(final Product product) {
        final ProductEvent productEvent = createProductEvent(product, ActionType.DELETE);

        sendMessage(productEvent);
    }

    @Override
    public void sendUpdate(final Product product) {
        final ProductEvent productEvent = createProductEvent(product, ActionType.UPDATE);

        sendMessage(productEvent);
    }

    private ProductEvent createProductEvent(final Product product, final ActionType actionType) {
        return new ProductEvent(
                product.getId(),
                product.getName(),
                product.getPrice(),
                actionType
        );
    }

    private void sendMessage(final ProductEvent productEvent) {
        kafkaTemplate.send("product-events", productEvent);
    }

}
