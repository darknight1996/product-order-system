package org.example.order.service;

import java.util.List;
import org.example.order.dto.OrderAddDTO;
import org.example.order.entity.Order;
import org.springframework.stereotype.Service;

@Service
public interface OrderService {

  List<Order> getAll();

  Order addOrder(OrderAddDTO orderAddDTO);
}
