package org.example.inventory.unit.message;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.example.inventory.message.impl.KafkaProductMessageService;
import org.example.inventory.service.InventoryService;
import org.example.inventory.util.ProductInitializer;
import org.example.message.ActionType;
import org.example.message.Product;
import org.example.message.ProductEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class KafkaProductMessageServiceTest {

  @Mock private InventoryService inventoryService;

  @InjectMocks private KafkaProductMessageService cut;

  @Test
  void productEvent_add_shouldAddProductToInventory() {
    Product product = ProductInitializer.createProduct();
    ProductEvent productEvent = new ProductEvent(product, ActionType.ADD);

    cut.productEvent(productEvent);

    verify(inventoryService, times(1))
        .createInventory(product.getId(), product.getName(), product.getPrice());
  }

  @Test
  void productEvent_delete_shouldDeleteInventory() {
    Product product = ProductInitializer.createProduct();
    ProductEvent productEvent = new ProductEvent(product, ActionType.DELETE);

    cut.productEvent(productEvent);

    verify(inventoryService, times(1)).deleteByProductId(product.getId());
  }

  @Test
  void productEvent_update_shouldUpdateInventory() {
    Product product = ProductInitializer.createProduct();
    ProductEvent productEvent = new ProductEvent(product, ActionType.UPDATE);

    cut.productEvent(productEvent);

    verify(inventoryService, times(1))
        .updateProductDetails(product.getId(), product.getName(), product.getPrice());
  }
}
