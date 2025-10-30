package org.example.inventory.unit.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.example.inventory.controller.InventoryController;
import org.example.inventory.dto.InventoryUpdateDTO;
import org.example.inventory.dto.OrderDTO;
import org.example.inventory.entity.Inventory;
import org.example.inventory.service.InventoryService;
import org.example.inventory.util.InventoryInitializer;
import org.example.inventory.util.OrderInitializer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(InventoryController.class)
class InventoryControllerTest {

  private static final String INVENTORY_URL = "/api/v1/inventory";
  private static final String INVENTORY_ALL_URL = INVENTORY_URL + "/all";
  private static final String INVENTORY_ADJUST_URL = INVENTORY_URL + "/adjust";
  private static final String INVALID_JSON = "Invalid JSON";

  @MockitoBean private InventoryService inventoryService;

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @Test
  void getAll_shouldReturnAllInventories() throws Exception {
    final List<Inventory> inventories = InventoryInitializer.createInventories();

    when(inventoryService.getAll()).thenReturn(inventories);

    final ResultActions resultActions = mockMvc.perform(get(INVENTORY_ALL_URL));

    resultActions
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.size()").value(inventories.size()));

    for (int i = 0; i < inventories.size(); i++) {
      final Inventory inventory = inventories.get(i);

      resultActions
          .andExpect(jsonPath(String.format("$[%d].id", i)).value(inventory.getId()))
          .andExpect(jsonPath(String.format("$[%d].productId", i)).value(inventory.getProductId()))
          .andExpect(
              jsonPath(String.format("$[%d].productName", i)).value(inventory.getProductName()))
          .andExpect(
              jsonPath(String.format("$[%d].productPrice", i)).value(inventory.getProductPrice()))
          .andExpect(jsonPath(String.format("$[%d].quantity", i)).value(inventory.getQuantity()));
    }
  }

  @Test
  void update_shouldUpdateInventory() throws Exception {
    final Inventory inventory = InventoryInitializer.createInventory();
    final InventoryUpdateDTO inventoryUpdateDTO = InventoryInitializer.createInventoryUpdateDTO();
    final String json = objectMapper.writeValueAsString(inventoryUpdateDTO);

    when(inventoryService.updateQuantity(any(), any())).thenReturn(inventory);

    mockMvc
        .perform(put(INVENTORY_URL).contentType(MediaType.APPLICATION_JSON).content(json))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.id").value(inventory.getId()))
        .andExpect(jsonPath("$.productId").value(inventory.getProductId()))
        .andExpect(jsonPath("$.productName").value(inventory.getProductName()))
        .andExpect(jsonPath("$.productPrice").value(inventory.getProductPrice()))
        .andExpect(jsonPath("$.quantity").value(inventory.getQuantity()));
  }

  @Test
  void update_invalidRequestBody() throws Exception {
    mockMvc
        .perform(put(INVENTORY_URL).contentType(MediaType.APPLICATION_JSON).content(INVALID_JSON))
        .andExpect(status().isBadRequest());
  }

  @Test
  void adjustInventory_shouldAdjustInventory() throws Exception {
    final OrderDTO orderDTO = OrderInitializer.createOrderDTO();
    final String json = objectMapper.writeValueAsString(orderDTO);

    when(inventoryService.adjustInventory(any())).thenReturn(true);

    mockMvc
        .perform(post(INVENTORY_ADJUST_URL).contentType(MediaType.APPLICATION_JSON).content(json))
        .andExpect(status().isOk());
  }

  @Test
  void adjustInventory_insufficientInventory() throws Exception {
    final OrderDTO orderDTO = OrderInitializer.createOrderDTO();
    final String json = objectMapper.writeValueAsString(orderDTO);

    when(inventoryService.adjustInventory(any())).thenReturn(false);

    mockMvc
        .perform(post(INVENTORY_ADJUST_URL).contentType(MediaType.APPLICATION_JSON).content(json))
        .andExpect(status().isConflict());
  }
}
