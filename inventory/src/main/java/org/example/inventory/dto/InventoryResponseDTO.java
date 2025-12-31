package org.example.inventory.dto;

import java.math.BigDecimal;

public record InventoryResponseDTO(
    Long id, Long productId, String productName, BigDecimal productPrice, Integer quantity) {}
