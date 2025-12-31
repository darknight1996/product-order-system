package org.example.inventory.service;

import java.math.BigDecimal;
import java.util.List;
import org.example.inventory.dto.OrderDTO;
import org.example.inventory.entity.Inventory;
import org.springframework.stereotype.Service;

@Service
public interface InventoryService {

  List<Inventory> getAll();

  Inventory updateQuantity(Long id, Integer quantity);

  void adjustInventory(OrderDTO orderDTO);

  void createInventory(Long productId, String productName, BigDecimal productPrice);

  void updateProductDetails(Long productId, String productName, BigDecimal productPrice);

  void deleteByProductId(Long productId);
}
