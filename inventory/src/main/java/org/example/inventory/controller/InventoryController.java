package org.example.inventory.controller;

import java.util.List;
import org.example.inventory.dto.InventoryUpdateDTO;
import org.example.inventory.dto.OrderDTO;
import org.example.inventory.entity.Inventory;
import org.example.inventory.service.InventoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/inventory")
public class InventoryController {

  private final InventoryService inventoryService;

  public InventoryController(InventoryService inventoryService) {
    this.inventoryService = inventoryService;
  }

  @GetMapping("/all")
  public ResponseEntity<List<Inventory>> getAll() {
    return ResponseEntity.ok().body(inventoryService.getAll());
  }

  @PutMapping
  public ResponseEntity<Inventory> update(@RequestBody InventoryUpdateDTO inventoryUpdateDTO) {
    Inventory inventory =
        inventoryService.updateQuantity(
            inventoryUpdateDTO.getId(), inventoryUpdateDTO.getQuantity());

    return ResponseEntity.ok().body(inventory);
  }

  @PostMapping("/adjust")
  public ResponseEntity<String> adjustInventory(@RequestBody OrderDTO orderDTO) {
    boolean success = inventoryService.adjustInventory(orderDTO);

    if (success) {
      return ResponseEntity.ok("Inventory adjusted successfully.");
    } else {
      return ResponseEntity.status(HttpStatus.CONFLICT).body("Insufficient inventory.");
    }
  }
}
