package org.example.order.controller;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.order.dto.OrderAddDTO;
import org.example.order.dto.OrderResponseDTO;
import org.example.order.entity.Order;
import org.example.order.mapper.OrderMapper;
import org.example.order.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

  private final OrderService orderService;
  private final OrderMapper orderMapper;

  @GetMapping
  public ResponseEntity<List<OrderResponseDTO>> getAll() {
    List<Order> orders = orderService.getAll();

    return ResponseEntity.ok(orderMapper.toDtoList(orders));
  }

  @PostMapping
  public ResponseEntity<OrderResponseDTO> add(@Valid @RequestBody OrderAddDTO orderAddDTO) {
    Order createdOrder = orderService.addOrder(orderAddDTO);

    OrderResponseDTO orderResponseDTO = orderMapper.toDto(createdOrder);

    return ResponseEntity.status(HttpStatus.CREATED).body(orderResponseDTO);
  }
}
