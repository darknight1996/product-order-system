package org.example.order.dto.external;

import java.math.BigDecimal;

public record ProductResponseDTO(Long id, String name, String description, BigDecimal price) {}
