package org.example.order.dto;

import java.math.BigDecimal;

public record OrderResponseDTO(
    Long id, Long productId, Integer quantity, BigDecimal totalCost, String status) {}
