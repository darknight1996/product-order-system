package org.example.catalog.dto;

import java.math.BigDecimal;

public record ProductAddDTO(String name, String description, BigDecimal price) {}
