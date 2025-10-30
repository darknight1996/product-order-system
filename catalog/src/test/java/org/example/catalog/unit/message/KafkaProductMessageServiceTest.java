package org.example.catalog.unit.message;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.example.catalog.entity.Product;
import org.example.catalog.message.impl.KafkaProductMessageService;
import org.example.catalog.util.ProductInitializer;
import org.example.message.ActionType;
import org.example.message.ProductEvent;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

@ExtendWith(MockitoExtension.class)
class KafkaProductMessageServiceTest {

  @Mock private KafkaTemplate<String, ProductEvent> kafkaTemplate;

  @InjectMocks private KafkaProductMessageService cut;

  @ParameterizedTest
  @EnumSource(ActionType.class)
  void sendMessage_shouldSendMessage(final ActionType actionType) {
    final Product mockedProduct = ProductInitializer.createProduct();
    final ArgumentCaptor<ProductEvent> capturedProductEvent =
        ArgumentCaptor.forClass(ProductEvent.class);

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
}
