package org.example.inventory.controller;

import org.example.inventory.dto.InventoryUpdateDTO;
import org.example.inventory.entity.Inventory;
import org.example.inventory.service.InventoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(final InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping("/all")
    public List<Inventory> getAll() {
        return inventoryService.getAll();
    }

    @PutMapping
    public ResponseEntity<Inventory> update(@RequestBody final InventoryUpdateDTO inventoryUpdateDTO) {
        final Inventory inventory = inventoryService.
                updateQuantity(inventoryUpdateDTO.getId(), inventoryUpdateDTO.getQuantity());

        return ResponseEntity.ok().body(inventory);
    }

}
