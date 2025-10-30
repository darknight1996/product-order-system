package org.example.inventory.dto;

public class InventoryUpdateDTO {

  private Long id;

  private Integer quantity;

  public InventoryUpdateDTO() {}

  public InventoryUpdateDTO(final Long id, final Integer quantity) {
    this.id = id;
    this.quantity = quantity;
  }

  public Long getId() {
    return id;
  }

  public void setId(final Long id) {
    this.id = id;
  }

  public Integer getQuantity() {
    return quantity;
  }

  public void setQuantity(final Integer quantity) {
    this.quantity = quantity;
  }
}
