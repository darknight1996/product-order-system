package org.example.order.controller;

import org.example.order.dto.OrderAddDTO;
import org.example.order.entity.Order;
import org.example.order.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/order")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Order>> getAll() {
        return ResponseEntity.ok(orderService.getAll());
    }

    @PostMapping
    public ResponseEntity<Order> add(@RequestBody OrderAddDTO orderAddDTO) {
        return ResponseEntity.ok(orderService.addOrder(orderAddDTO));
    }
}
