package org.example.inventory.util;

import java.math.BigDecimal;
import java.util.List;
import org.example.inventory.dto.InventoryUpdateDTO;
import org.example.inventory.entity.Inventory;

public class InventoryInitializer {

  public static List<Inventory> createInventories() {
    return List.of(
        new Inventory(1L, "product 1", BigDecimal.valueOf(1000), 10),
        new Inventory(2L, "product 2", BigDecimal.valueOf(2000), 10));
  }

  public static Inventory createInventory() {
    return new Inventory(1L, 1L, "product 1", BigDecimal.valueOf(1000), 10);
  }

  public static Inventory createUpdatedInventory() {
    final Inventory updatedInventory = createInventory();

    updatedInventory.setQuantity(updatedInventory.getQuantity() + 1);

    return updatedInventory;
  }

  public static InventoryUpdateDTO createInventoryUpdateDTO() {
    return new InventoryUpdateDTO(1L, 10);
  }
}
