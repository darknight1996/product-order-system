package org.example.inventory.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.inventory.dto.InventoryResponseDTO;
import org.example.inventory.dto.InventoryUpdateDTO;
import org.example.inventory.dto.OrderDTO;
import org.example.inventory.entity.Inventory;
import org.example.inventory.mapper.InventoryMapper;
import org.example.inventory.service.InventoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/inventory")
@RequiredArgsConstructor
public class InventoryController {

  private final InventoryService inventoryService;
  private final InventoryMapper inventoryMapper;

  @GetMapping
  public ResponseEntity<List<InventoryResponseDTO>> getAll() {
    List<Inventory> inventoryList = inventoryService.getAll();

    return ResponseEntity.ok(inventoryMapper.toInventoryResponseDTOList(inventoryList));
  }

  @PutMapping
  public ResponseEntity<InventoryResponseDTO> update(
      @RequestBody InventoryUpdateDTO inventoryUpdateDTO) {
    Inventory inventory =
        inventoryService.updateQuantity(inventoryUpdateDTO.id(), inventoryUpdateDTO.quantity());

    return ResponseEntity.ok(inventoryMapper.toInventoryResponseDTO(inventory));
  }

  @PostMapping("/adjust")
  public ResponseEntity<String> adjustInventory(@RequestBody OrderDTO orderDTO) {
    inventoryService.adjustInventory(orderDTO);

    return ResponseEntity.ok("Inventory adjusted successfully.");
  }
}
