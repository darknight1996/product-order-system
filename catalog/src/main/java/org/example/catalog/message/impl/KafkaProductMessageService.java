package org.example.catalog.message.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.example.catalog.entity.Product;
import org.example.catalog.message.ProductMessageService;
import org.example.catalog.message.model.ActionType;
import org.example.catalog.message.model.ProductEvent;
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
    public void sendAdd(Product product) {
        final ProductEvent productEvent = new ProductEvent(product, ActionType.ADD);

        sendMessage(productEvent);
    }

    @Override
    public void sendDelete(Product product) {
        final ProductEvent productEvent = new ProductEvent(product, ActionType.DELETE);

        sendMessage(productEvent);
    }

    @Override
    public void sendUpdate(Product product) {
        final ProductEvent productEvent = new ProductEvent(product, ActionType.UPDATE);

        sendMessage(productEvent);
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
