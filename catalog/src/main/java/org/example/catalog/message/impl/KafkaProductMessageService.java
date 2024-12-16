package org.example.catalog.message.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.example.catalog.entity.Product;
import org.example.catalog.message.ProductMessageService;
import org.example.message.ActionType;
import org.example.message.ProductEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProductMessageService implements ProductMessageService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public KafkaProductMessageService(final KafkaTemplate<String, String> kafkaTemplate,
                                      final ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
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
        final ObjectWriter objectWriter = objectMapper.writer().withDefaultPrettyPrinter();

        try {
            final String jsonContent = objectWriter.writeValueAsString(productEvent);
            kafkaTemplate.send("product-events", jsonContent);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
