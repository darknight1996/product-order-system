package org.example.order.service.impl;

import jakarta.transaction.Transactional;
import org.example.order.dto.OrderAddDTO;
import org.example.order.entity.Order;
import org.example.order.repository.OrderRepository;
import org.example.order.service.CatalogClient;
import org.example.order.service.InventoryClient;
import org.example.order.service.OrderService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CatalogClient catalogClient;
    private final InventoryClient inventoryClient;

    public OrderServiceImpl(OrderRepository orderRepository, CatalogClient catalogClient, InventoryClient inventoryClient) {
        this.orderRepository = orderRepository;
        this.catalogClient = catalogClient;
        this.inventoryClient = inventoryClient;
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

        final BigDecimal productPrice = catalogClient.getProductPrice(productId);
        final BigDecimal totalCost = productPrice.multiply(BigDecimal.valueOf(quantity));

        inventoryClient.adjustInventory(orderAddDTO);

        final Order order = new Order();
        order.setProductId(productId);
        order.setQuantity(quantity);
        order.setTotalCost(totalCost);

        return orderRepository.save(order);
    }

}
