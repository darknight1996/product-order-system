package org.example.inventory.unit.message;

import org.example.inventory.entity.Inventory;
import org.example.inventory.message.impl.KafkaProductMessageService;
import org.example.inventory.repository.InventoryRepository;
import org.example.inventory.util.ProductInitializer;
import org.example.message.ActionType;
import org.example.message.Product;
import org.example.message.ProductEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class KafkaProductMessageServiceTest {

    @Mock
    private InventoryRepository inventoryRepository;

    @InjectMocks
    private KafkaProductMessageService cut;

    @Test
    void productEvent_add_shouldAddProductToInventory() {
        final Product product = ProductInitializer.createProduct();
        final ProductEvent productEvent = new ProductEvent(product, ActionType.ADD);

        when(inventoryRepository.findByProductId(product.getId())).thenReturn(Optional.empty());

        cut.productEvent(productEvent);

        final ArgumentCaptor<Inventory> inventoryCaptor = ArgumentCaptor.forClass(Inventory.class);
        verify(inventoryRepository, times(1)).save(inventoryCaptor.capture());
        final Inventory capturedInventory = inventoryCaptor.getValue();
        final Inventory expectedInventory = new Inventory(product.getId(), product.getName(), product.getPrice(), 0);

        assertEquals(expectedInventory, capturedInventory);
    }

    @Test
    void productEvent_add_shouldNotAddProductToInventoryIfInventoryExists() {
        final Product product = ProductInitializer.createProduct();
        final ProductEvent productEvent = new ProductEvent(product, ActionType.ADD);
        final Inventory existedInventory = new Inventory(product.getId(), product.getName(), product.getPrice(), 0);

        when(inventoryRepository.findByProductId(product.getId())).thenReturn(Optional.of(existedInventory));

        cut.productEvent(productEvent);

        verify(inventoryRepository, never()).save(any());
    }

    @Test
    void productEvent_delete_shouldDeleteInventory() {
        final Product product = ProductInitializer.createProduct();
        final ProductEvent productEvent = new ProductEvent(product, ActionType.DELETE);

        cut.productEvent(productEvent);

        verify(inventoryRepository, times(1)).deleteByProductId(product.getId());
    }

    @Test
    void productEvent_update_shouldUpdateInventory() {
        final Product product = ProductInitializer.createProduct();
        final Product updatedProduct = ProductInitializer.createUpdatedProduct();
        final ProductEvent productEvent = new ProductEvent(updatedProduct, ActionType.UPDATE);
        final Inventory existedInventory = new Inventory(
                product.getId(),
                product.getName(),
                product.getPrice(),
                0
        );
        final Inventory updatedInventory = new Inventory(
                updatedProduct.getId(),
                updatedProduct.getName(),
                updatedProduct.getPrice(),
                0
        );

        when(inventoryRepository.findByProductId(product.getId())).thenReturn(Optional.of(existedInventory));

        cut.productEvent(productEvent);

        final ArgumentCaptor<Inventory> inventoryCaptor = ArgumentCaptor.forClass(Inventory.class);
        verify(inventoryRepository, times(1)).save(inventoryCaptor.capture());
        final Inventory capturedInventory = inventoryCaptor.getValue();

        assertEquals(updatedInventory, capturedInventory);
    }
}
