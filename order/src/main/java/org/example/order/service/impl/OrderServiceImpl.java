package org.example.order.service.impl;

import feign.FeignException;
import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.order.client.CatalogClient;
import org.example.order.client.InventoryClient;
import org.example.order.dto.OrderAddDTO;
import org.example.order.dto.external.ProductResponseDTO;
import org.example.order.entity.Order;
import org.example.order.exception.InsufficientInventoryException;
import org.example.order.repository.OrderRepository;
import org.example.order.service.OrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

  private final OrderRepository orderRepository;
  private final CatalogClient catalogClient;
  private final InventoryClient inventoryClient;

  @Override
  @Transactional(readOnly = true)
  public List<Order> getAll() {
    return orderRepository.findAll();
  }

  @Override
  @Transactional
  public Order addOrder(OrderAddDTO orderAddDTO) {
    Long productId = orderAddDTO.productId();
    Integer quantity = orderAddDTO.quantity();

    log.info("Processing order: productId={}, quantity={}", productId, quantity);

    ProductResponseDTO productDto = catalogClient.getProductById(productId);

    BigDecimal totalCost = productDto.price().multiply(BigDecimal.valueOf(quantity));

    adjustInventory(orderAddDTO);

    Order order =
        Order.builder().productId(productId).quantity(quantity).totalCost(totalCost).build();

    return orderRepository.save(order);
  }

  private void adjustInventory(OrderAddDTO orderAddDTO) {
    try {
      inventoryClient.adjustInventory(orderAddDTO);
    } catch (FeignException.Conflict e) {
      log.warn("Not enough stock for productId: {}", orderAddDTO.productId());
      throw new InsufficientInventoryException("Not enough stock available");
    }
  }
}
