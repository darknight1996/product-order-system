package org.example.message;

import java.math.BigDecimal;
import lombok.Builder;

@Builder(toBuilder = true)
public record Product(Long id, String name, BigDecimal price) {}
