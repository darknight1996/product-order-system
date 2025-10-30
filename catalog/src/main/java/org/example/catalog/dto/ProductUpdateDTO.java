package org.example.catalog.dto;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public class ProductUpdateDTO extends ProductAddDTO {

  @NotNull(message = "id is mandatory")
  private Long id;

  public ProductUpdateDTO() {}

  public ProductUpdateDTO(
      final Long id, final String name, final String description, final BigDecimal price) {
    super(name, description, price);
    this.id = id;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }
}
