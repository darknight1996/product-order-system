package org.example.catalog.unit.message;

import org.example.catalog.entity.Product;
import org.example.catalog.message.impl.KafkaProductMessageService;
import org.example.message.ActionType;
import org.example.message.ProductEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class KafkaProductMessageServiceTest {

    @Mock
    private KafkaTemplate<String, ProductEvent> kafkaTemplate;

    @InjectMocks
    private KafkaProductMessageService cut;

    @Test
    public void sendAdd_shouldSendMessage() {
        sendMessage(ActionType.ADD);
    }

    @Test
    public void sendDelete_shouldSendMessage() {
        sendMessage(ActionType.DELETE);
    }

    @Test
    public void sendUpdate_shouldSendMessage() {
        sendMessage(ActionType.UPDATE);
    }

    private void sendMessage(ActionType actionType) {
        final Product mockedProduct = createProduct();
        final ArgumentCaptor<ProductEvent> capturedProductEvent = ArgumentCaptor.forClass(ProductEvent.class);

        switch (actionType) {
            case ADD -> cut.sendAdd(mockedProduct);
            case DELETE -> cut.sendDelete(mockedProduct);
            case UPDATE -> cut.sendUpdate(mockedProduct);
        }

        verify(kafkaTemplate, times(1)).send(eq("product-events"), capturedProductEvent.capture());

        final ProductEvent productEvent = capturedProductEvent.getValue();

        assertEquals(mockedProduct.getId(), productEvent.getProduct().getId());
        assertEquals(mockedProduct.getName(), productEvent.getProduct().getName());
        assertEquals(mockedProduct.getPrice(), productEvent.getProduct().getPrice());

        assertEquals(actionType, productEvent.getActionType());
    }

    private Product createProduct() {
        return new Product(1L, "name", "description", BigDecimal.valueOf(1000));
    }

}
