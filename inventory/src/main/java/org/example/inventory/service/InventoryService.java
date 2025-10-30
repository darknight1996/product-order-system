package org.example.inventory.service;

import java.util.List;
import org.example.inventory.dto.OrderDTO;
import org.example.inventory.entity.Inventory;
import org.springframework.stereotype.Service;

@Service
public interface InventoryService {

  List<Inventory> getAll();

  Inventory getById(final Long id);

  Inventory add(final Inventory inventory);

  void delete(final Long id);

  Inventory updateQuantity(final Long id, final Integer quantity);

  boolean adjustInventory(final OrderDTO orderDTO);
}
