package org.example.order.service;

import org.example.order.dto.OrderAddDTO;
import org.example.order.entity.Order;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface OrderService {

    List<Order> getAll();
    Order addOrder(OrderAddDTO orderAddDTO);

}
