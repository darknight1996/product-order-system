package org.example.inventory.dto;

public class InventoryUpdateDTO {

    private Integer quantity;

    public InventoryUpdateDTO() {
    }

    public InventoryUpdateDTO(final Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(final Integer quantity) {
        this.quantity = quantity;
    }

}
