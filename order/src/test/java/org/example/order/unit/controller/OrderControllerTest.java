package org.example.order.unit.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.example.order.controller.OrderController;
import org.example.order.dto.OrderAddDTO;
import org.example.order.entity.Order;
import org.example.order.mapper.OrderMapper;
import org.example.order.mapper.OrderMapperImpl;
import org.example.order.service.OrderService;
import org.example.order.util.OrderTestDataFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(OrderController.class)
@Import(OrderMapperImpl.class)
class OrderControllerTest {

  private static final String BASE_URL = "/api/v1/orders";

  @MockitoBean private OrderService orderService;

  @MockitoSpyBean private OrderMapper orderMapper;

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @Test
  @DisplayName("GET " + BASE_URL + " - Should return list of all orders")
  void getAll_shouldReturnAllOrders() throws Exception {
    List<Order> orders = OrderTestDataFactory.createOrders();
    when(orderService.getAll()).thenReturn(orders);

    mockMvc
        .perform(get(BASE_URL))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.size()").value(2))
        .andExpect(jsonPath("$[0].id").value(orders.get(0).getId()))
        .andExpect(jsonPath("$[0].productId").value(101))
        .andExpect(jsonPath("$[0].totalCost").value(150.00))
        .andExpect(jsonPath("$[1].id").value(orders.get(1).getId()))
        .andExpect(jsonPath("$[1].status").value("COMPLETED"));
  }

  @Test
  @DisplayName("POST " + BASE_URL + " - Should successfully create an order")
  void add_shouldCreateOrderAndReturn201() throws Exception {
    OrderAddDTO addDTO = OrderTestDataFactory.createOrderAddDTO();
    Order createdOrder = OrderTestDataFactory.createOrder();
    String jsonRequest = objectMapper.writeValueAsString(addDTO);

    when(orderService.addOrder(any(OrderAddDTO.class))).thenReturn(createdOrder);

    mockMvc
        .perform(post(BASE_URL).contentType(MediaType.APPLICATION_JSON).content(jsonRequest))
        .andExpect(status().isCreated())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.id").value(createdOrder.getId()))
        .andExpect(jsonPath("$.productId").value(createdOrder.getProductId()))
        .andExpect(jsonPath("$.quantity").value(createdOrder.getQuantity()))
        .andExpect(jsonPath("$.status").value("CREATED"));
  }

  @Test
  @DisplayName("POST " + BASE_URL + " - Should return 400 when body is invalid")
  void add_shouldReturnBadRequestOnInvalidData() throws Exception {
    String invalidJson = "{\"productId\": null, \"quantity\": 0}";

    mockMvc
        .perform(post(BASE_URL).contentType(MediaType.APPLICATION_JSON).content(invalidJson))
        .andExpect(status().isBadRequest());
  }
}
