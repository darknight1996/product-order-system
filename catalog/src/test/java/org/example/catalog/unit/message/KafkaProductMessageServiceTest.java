package org.example.catalog.unit.message;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.concurrent.CompletableFuture;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.example.catalog.entity.Product;
import org.example.catalog.message.impl.KafkaProductMessageService;
import org.example.catalog.unit.util.ProductInitializer;
import org.example.message.ActionType;
import org.example.message.ProductEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class KafkaProductMessageServiceTest {

  @Mock private KafkaTemplate<String, ProductEvent> kafkaTemplate;

  @InjectMocks private KafkaProductMessageService cut;

  @BeforeEach
  void setUp() {
    ReflectionTestUtils.setField(cut, "topicName", "product-events");
  }

  @ParameterizedTest
  @EnumSource(ActionType.class)
  void sendMessage_shouldSendMessageSuccess(ActionType actionType) {
    RecordMetadata metadata = mock(RecordMetadata.class);
    when(metadata.offset()).thenReturn(100L);

    SendResult<String, ProductEvent> sendResult = new SendResult<>(null, metadata);

    when(kafkaTemplate.send(any(), any(), any()))
        .thenReturn(CompletableFuture.completedFuture(sendResult));

    Product mockedProduct = ProductInitializer.createProduct();
    ArgumentCaptor<ProductEvent> capturedProductEvent = ArgumentCaptor.forClass(ProductEvent.class);

    switch (actionType) {
      case ADD -> cut.sendAdd(mockedProduct);
      case DELETE -> cut.sendDelete(mockedProduct);
      case UPDATE -> cut.sendUpdate(mockedProduct);
    }

    verify(kafkaTemplate, times(1))
        .send(eq("product-events"), any(), capturedProductEvent.capture());

    ProductEvent productEvent = capturedProductEvent.getValue();

    assertEquals(mockedProduct.getId(), productEvent.getProduct().getId());
    assertEquals(mockedProduct.getName(), productEvent.getProduct().getName());
    assertEquals(actionType, productEvent.getActionType());
  }

  @Test
  void sendMessage_shouldHandleFailure() {
    when(kafkaTemplate.send(any(), any(), any()))
        .thenReturn(CompletableFuture.failedFuture(new RuntimeException("Kafka is down")));

    Product mockedProduct = ProductInitializer.createProduct();

    cut.sendAdd(mockedProduct);

    verify(kafkaTemplate, times(1)).send(eq("product-events"), any(), any());
  }
}
