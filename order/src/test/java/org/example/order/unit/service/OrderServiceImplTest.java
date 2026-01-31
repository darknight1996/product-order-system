package org.example.order.unit.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import feign.FeignException;
import java.math.BigDecimal;
import org.example.order.client.CatalogClient;
import org.example.order.client.InventoryClient;
import org.example.order.dto.OrderAddDTO;
import org.example.order.dto.external.ProductResponseDTO;
import org.example.order.entity.Order;
import org.example.order.exception.InsufficientInventoryException;
import org.example.order.repository.OrderRepository;
import org.example.order.service.impl.OrderServiceImpl;
import org.example.order.util.OrderTestDataFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

  @Mock private OrderRepository orderRepository;
  @Mock private CatalogClient catalogClient;
  @Mock private InventoryClient inventoryClient;

  @InjectMocks private OrderServiceImpl orderService;

  @Test
  void addOrder_shouldSaveOrder_whenValidRequest() {
    OrderAddDTO addDTO = OrderTestDataFactory.createOrderAddDTO();
    ProductResponseDTO productMock = OrderTestDataFactory.createProductResponseDTO();

    when(catalogClient.getProductById(addDTO.productId())).thenReturn(productMock);
    when(orderRepository.save(any(Order.class))).thenAnswer(i -> i.getArgument(0));

    Order result = orderService.addOrder(addDTO);

    assertEquals(new BigDecimal("200.00"), result.getTotalCost());
    verify(inventoryClient).adjustInventory(addDTO);
    verify(orderRepository).save(any(Order.class));
  }

  @Test
  void addOrder_shouldThrowException_whenInventoryFails() {
    OrderAddDTO addDTO = OrderTestDataFactory.createOrderAddDTO();
    ProductResponseDTO productMock = OrderTestDataFactory.createProductResponseDTO();

    when(catalogClient.getProductById(anyLong())).thenReturn(productMock);

    doThrow(mock(FeignException.Conflict.class))
        .when(inventoryClient)
        .adjustInventory(any(OrderAddDTO.class));

    assertThrows(InsufficientInventoryException.class, () -> orderService.addOrder(addDTO));
    verify(orderRepository, never()).save(any());
  }
}
