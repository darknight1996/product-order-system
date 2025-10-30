package org.example.order.service.impl;

import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import org.example.order.dto.OrderAddDTO;
import org.example.order.entity.Order;
import org.example.order.repository.OrderRepository;
import org.example.order.service.CatalogService;
import org.example.order.service.InventoryService;
import org.example.order.service.OrderService;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {

  private final OrderRepository orderRepository;
  private final CatalogService catalogService;
  private final InventoryService inventoryService;

  public OrderServiceImpl(
      OrderRepository orderRepository,
      CatalogService catalogService,
      InventoryService inventoryService) {
    this.orderRepository = orderRepository;
    this.catalogService = catalogService;
    this.inventoryService = inventoryService;
  }

  @Override
  public List<Order> getAll() {
    return orderRepository.findAll();
  }

  @Override
  @Transactional
  public Order addOrder(OrderAddDTO orderAddDTO) {
    final Long productId = orderAddDTO.getProductId();
    final Integer quantity = orderAddDTO.getQuantity();

    final BigDecimal productPrice = catalogService.getProductPrice(productId);
    final BigDecimal totalCost = productPrice.multiply(BigDecimal.valueOf(quantity));

    inventoryService.adjustInventory(orderAddDTO);

    final Order order = new Order();
    order.setProductId(productId);
    order.setQuantity(quantity);
    order.setTotalCost(totalCost);

    return orderRepository.save(order);
  }
}
