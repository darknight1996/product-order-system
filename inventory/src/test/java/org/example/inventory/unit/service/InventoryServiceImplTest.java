package org.example.inventory.unit.service;

import jakarta.persistence.EntityNotFoundException;
import org.example.inventory.dto.OrderDTO;
import org.example.inventory.entity.Inventory;
import org.example.inventory.repository.InventoryRepository;
import org.example.inventory.service.impl.InventoryServiceImpl;
import org.example.inventory.util.InventoryInitializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InventoryServiceImplTest {

    @Mock
    private InventoryRepository inventoryRepository;

    @InjectMocks
    private InventoryServiceImpl cut;

    private Inventory inventory;
    private Inventory updatedInventory;
    private List<Inventory> inventories;

    @BeforeEach
    void init() {
        inventory = InventoryInitializer.createInventory();
        updatedInventory = InventoryInitializer.createUpdatedInventory();
        inventories = InventoryInitializer.createInventories();
    }

    @Test
    void getAll_shouldReturnAllInventories() {
        when(inventoryRepository.findAll()).thenReturn(inventories);

        final List<Inventory> result = cut.getAll();

        assertEquals(inventories, result);

        verify(inventoryRepository, times(1)).findAll();
    }

    @Test
    void getById_shouldReturnInventoryById() {
        when(inventoryRepository.findById(inventory.getId())).thenReturn(Optional.of(inventory));

        final Inventory result = cut.getById(inventory.getId());

        assertEquals(inventory, result);

        verify(inventoryRepository, times(1)).findById(inventory.getId());
    }

    @Test
    void getByProductId_productNotFound() {
        when(inventoryRepository.findById(inventory.getId())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> cut.getById(inventory.getId()));

        verify(inventoryRepository, times(1)).findById(inventory.getId());
    }

    @Test
    void add_shouldAddInventory() {
        when(inventoryRepository.save(inventory)).thenReturn(inventory);

        final Inventory result = cut.add(inventory);

        assertEquals(inventory, result);

        verify(inventoryRepository, times(1)).save(inventory);
    }

    @Test
    void delete_shouldDeleteInventory() {
        when(inventoryRepository.findById(inventory.getId())).thenReturn(Optional.of(inventory));

        cut.delete(inventory.getId());

        verify(inventoryRepository, times(1)).delete(inventory);
    }

    @Test
    void delete_productNotFound() {
        when(inventoryRepository.findById(inventory.getId())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> cut.delete(inventory.getId()));

        verify(inventoryRepository, never()).deleteById(inventory.getId());
    }

    @Test
    void updateQuantity_shouldUpdateInventoryQuantity() {
        when(inventoryRepository.findById(inventory.getId())).thenReturn(Optional.of(inventory));
        when(inventoryRepository.save(inventory)).thenReturn(updatedInventory);

        final Inventory result = cut.updateQuantity(updatedInventory.getId(), updatedInventory.getQuantity());

        assertEquals(updatedInventory, result);

        final ArgumentCaptor<Inventory> inventoryCaptor = ArgumentCaptor.forClass(Inventory.class);
        verify(inventoryRepository, times(1)).save(inventoryCaptor.capture());
        final Inventory capturedInventory = inventoryCaptor.getValue();

        assertEquals(updatedInventory, capturedInventory);
    }

    @Test
    void adjustInventory_shouldAdjustInventory() {
        when(inventoryRepository.findByProductId(inventory.getProductId())).thenReturn(Optional.of(inventory));

        final OrderDTO orderDTO = new OrderDTO(inventory.getProductId(), inventory.getQuantity() - 1);
        final Integer expectedQuantity = inventory.getQuantity() - orderDTO.getQuantity();

        final boolean result = cut.adjustInventory(orderDTO);

        assertTrue(result);

        final ArgumentCaptor<Inventory> inventoryCaptor = ArgumentCaptor.forClass(Inventory.class);
        verify(inventoryRepository, times(1)).save(inventoryCaptor.capture());
        final Inventory capturedInventory = inventoryCaptor.getValue();

        assertEquals(expectedQuantity, capturedInventory.getQuantity());
    }

    @Test
    void adjustInventory_insufficientInventory() {
        when(inventoryRepository.findByProductId(inventory.getProductId())).thenReturn(Optional.of(inventory));

        final OrderDTO orderDTO = new OrderDTO(inventory.getProductId(), inventory.getQuantity() + 1);

        final boolean result = cut.adjustInventory(orderDTO);

        assertFalse(result);

        verify(inventoryRepository, never()).save(inventory);
    }

}
