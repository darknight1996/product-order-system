package org.example.order.util;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.example.order.dto.OrderAddDTO;
import org.example.order.dto.external.ProductResponseDTO;
import org.example.order.entity.Order;
import org.example.order.entity.enums.OrderStatus;

public class OrderTestDataFactory {

  public static Order createOrder() {
    return Order.builder()
        .id(1L)
        .productId(101L)
        .quantity(2)
        .totalCost(new BigDecimal("150.00"))
        .status(OrderStatus.CREATED)
        .createdAt(LocalDateTime.now())
        .build();
  }

  public static List<Order> createOrders() {
    return List.of(
        createOrder(),
        Order.builder()
            .id(2L)
            .productId(102L)
            .quantity(5)
            .totalCost(new BigDecimal("500.00"))
            .status(OrderStatus.COMPLETED)
            .createdAt(LocalDateTime.now())
            .build());
  }

  public static OrderAddDTO createOrderAddDTO() {
    return new OrderAddDTO(101L, 2);
  }

  public static ProductResponseDTO createProductResponseDTO() {
    return new ProductResponseDTO(101L, "Test Product", "Description", new BigDecimal("100.00"));
  }
}
