package org.example.inventory.service;

import java.util.List;
import org.example.inventory.dto.OrderDTO;
import org.example.inventory.entity.Inventory;
import org.springframework.stereotype.Service;

@Service
public interface InventoryService {

  List<Inventory> getAll();

  Inventory getById(Long id);

  Inventory add(Inventory inventory);

  void delete(Long id);

  Inventory updateQuantity(Long id, Integer quantity);

  boolean adjustInventory(OrderDTO orderDTO);
}
