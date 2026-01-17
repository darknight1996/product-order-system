package org.example.inventory.unit.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.example.inventory.dto.OrderDTO;
import org.example.inventory.entity.Inventory;
import org.example.inventory.exception.InsufficientStockException;
import org.example.inventory.repository.InventoryRepository;
import org.example.inventory.service.impl.InventoryServiceImpl;
import org.example.inventory.util.InventoryInitializer;
import org.example.inventory.util.ProductInitializer;
import org.example.message.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class InventoryServiceImplTest {

  @Mock private InventoryRepository inventoryRepository;

  @InjectMocks private InventoryServiceImpl cut;

  private Inventory inventory;
  private List<Inventory> inventories;

  @BeforeEach
  void init() {
    inventory = InventoryInitializer.createInventory();
    inventories = InventoryInitializer.createInventories();
  }

  @Test
  void getAll_shouldReturnAllInventories() {
    when(inventoryRepository.findAll()).thenReturn(inventories);

    List<Inventory> result = cut.getAll();

    assertEquals(inventories, result);

    verify(inventoryRepository, times(1)).findAll();
  }

  @Test
  void createInventory_shouldCreateNewInventory_whenNotExists() {
    Product product = ProductInitializer.createProduct();

    when(inventoryRepository.findByProductId(product.getId())).thenReturn(Optional.empty());

    cut.createInventory(product.getId(), product.getName(), product.getPrice());

    ArgumentCaptor<Inventory> inventoryCaptor = ArgumentCaptor.forClass(Inventory.class);

    verify(inventoryRepository, times(1)).save(inventoryCaptor.capture());

    Inventory captured = inventoryCaptor.getValue();

    assertEquals(product.getId(), captured.getProductId());
    assertEquals(product.getName(), captured.getProductName());
    assertEquals(product.getPrice(), captured.getProductPrice());
    assertEquals(0, captured.getQuantity());
  }

  @Test
  void createInventory_shouldNotCreate_whenAlreadyExists() {
    when(inventoryRepository.findByProductId(inventory.getProductId()))
        .thenReturn(Optional.of(inventory));

    cut.createInventory(inventory.getProductId(), "Name", BigDecimal.TEN);

    verify(inventoryRepository, never()).save(any(Inventory.class));
  }

  @Test
  void deleteByProductId_shouldDeleteInventory() {
    when(inventoryRepository.findByProductId(inventory.getProductId()))
        .thenReturn(Optional.of(inventory));

    cut.deleteByProductId(inventory.getProductId());

    verify(inventoryRepository, times(1)).delete(inventory);
  }

  @Test
  void deleteByProductId_shouldThrow_whenNotFound() {
    Long productId = inventory.getProductId();

    when(inventoryRepository.findByProductId(productId)).thenReturn(Optional.empty());

    assertThrows(EntityNotFoundException.class, () -> cut.deleteByProductId(productId));

    verify(inventoryRepository, never()).delete(any());
  }

  @Test
  void updateQuantity_shouldUpdateAndReturnInventory() {
    Integer newQuantity = 50;

    when(inventoryRepository.findById(inventory.getId())).thenReturn(Optional.of(inventory));

    Inventory result = cut.updateQuantity(inventory.getId(), newQuantity);

    assertEquals(newQuantity, result.getQuantity());
    assertEquals(inventory, result);
  }

  @Test
  void updateProductDetails_shouldUpdateDetails() {
    String newName = "Updated Name";
    BigDecimal newPrice = BigDecimal.valueOf(99.99);

    when(inventoryRepository.findByProductId(inventory.getProductId()))
        .thenReturn(Optional.of(inventory));

    cut.updateProductDetails(inventory.getProductId(), newName, newPrice);

    assertEquals(newName, inventory.getProductName());
    assertEquals(newPrice, inventory.getProductPrice());
  }

  @Test
  void adjustInventory_shouldDeductQuantity_whenStockSufficient() {
    inventory.setQuantity(10);
    int deductAmount = 4;

    OrderDTO orderDTO = new OrderDTO(inventory.getProductId(), deductAmount);

    when(inventoryRepository.findByProductId(inventory.getProductId()))
        .thenReturn(Optional.of(inventory));

    cut.adjustInventory(orderDTO);

    assertEquals(6, inventory.getQuantity());
  }

  @Test
  void adjustInventory_shouldThrowException_whenInsufficientStock() {
    inventory.setQuantity(5);
    int requestAmount = 6;
    OrderDTO orderDTO = new OrderDTO(inventory.getProductId(), requestAmount);

    when(inventoryRepository.findByProductId(inventory.getProductId()))
        .thenReturn(Optional.of(inventory));

    assertThrows(InsufficientStockException.class, () -> cut.adjustInventory(orderDTO));

    assertEquals(5, inventory.getQuantity());
  }
}
