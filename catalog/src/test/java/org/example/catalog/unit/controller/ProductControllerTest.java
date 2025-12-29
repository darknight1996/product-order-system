package org.example.catalog.unit.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import org.example.catalog.controller.ProductController;
import org.example.catalog.entity.Product;
import org.example.catalog.mapper.ProductMapper;
import org.example.catalog.mapper.ProductMapperImpl;
import org.example.catalog.service.ProductService;
import org.example.catalog.unit.util.ProductInitializer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ProductController.class)
@Import(ProductMapperImpl.class)
class ProductControllerTest {

  private static final String PRODUCT_URL = "/api/v1/products";
  private static final String PRODUCT_BY_ID_URL = PRODUCT_URL + "/{id}";
  private static final String INVALID_JSON = "Invalid JSON";
  private static final String PRODUCT_NOT_FOUND = "Product not found";
  private static final Long PRODUCT_ID = 1L;

  @MockitoBean private ProductService productService;
  @MockitoSpyBean private ProductMapper productMapper;

  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper objectMapper;

  @Test
  void getAll_shouldReturnAllProducts() throws Exception {
    List<Product> mockedProducts = ProductInitializer.createProducts();
    when(productService.getAll()).thenReturn(mockedProducts);

    mockMvc
        .perform(get(PRODUCT_URL))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.size()").value(mockedProducts.size()))
        .andExpect(jsonPath("$[0].id").value(mockedProducts.get(0).getId()));
  }

  @Test
  void getById_shouldReturnProductById() throws Exception {
    Product mockedProduct = ProductInitializer.createProduct();
    when(productService.getById(mockedProduct.getId())).thenReturn(mockedProduct);

    mockMvc
        .perform(
            get(PRODUCT_BY_ID_URL, mockedProduct.getId()).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(mockedProduct.getId()));
  }

  @Test
  void getById_productNotFound() throws Exception {
    when(productService.getById(PRODUCT_ID))
        .thenThrow(new EntityNotFoundException(PRODUCT_NOT_FOUND));

    mockMvc
        .perform(get(PRODUCT_BY_ID_URL, PRODUCT_ID).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.message").value(PRODUCT_NOT_FOUND));
  }

  @Test
  void add_shouldAddProduct() throws Exception {
    Product mockedProduct = ProductInitializer.createProduct();
    String json = objectMapper.writeValueAsString(mockedProduct);

    when(productService.add(any(Product.class))).thenReturn(mockedProduct);

    mockMvc
        .perform(post(PRODUCT_URL).contentType(MediaType.APPLICATION_JSON).content(json))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(mockedProduct.getId()));
  }

  @Test
  void add_invalidRequestBody() throws Exception {
    mockMvc
        .perform(post(PRODUCT_URL).contentType(MediaType.APPLICATION_JSON).content(INVALID_JSON))
        .andExpect(status().isBadRequest());
  }

  @Test
  void delete_shouldDeleteProduct() throws Exception {
    mockMvc.perform(delete(PRODUCT_BY_ID_URL, PRODUCT_ID)).andExpect(status().isNoContent());
  }

  @Test
  void delete_productNotFound() throws Exception {
    doThrow(new EntityNotFoundException(PRODUCT_NOT_FOUND)).when(productService).delete(PRODUCT_ID);
    mockMvc.perform(delete(PRODUCT_BY_ID_URL, PRODUCT_ID)).andExpect(status().isNotFound());
  }

  @Test
  void update_shouldUpdateProduct() throws Exception {
    Product mockedProduct = ProductInitializer.createProduct();
    String json = objectMapper.writeValueAsString(mockedProduct);

    when(productService.update(any(Product.class))).thenReturn(mockedProduct);

    mockMvc
        .perform(
            put(PRODUCT_BY_ID_URL, PRODUCT_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(mockedProduct.getId()));
  }

  @Test
  void update_invalidRequestBody() throws Exception {
    mockMvc
        .perform(
            put(PRODUCT_BY_ID_URL, PRODUCT_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(INVALID_JSON))
        .andExpect(status().isBadRequest());
  }

  @Test
  void update_productNotFound() throws Exception {
    Product mockedProduct = ProductInitializer.createProduct();
    String json = objectMapper.writeValueAsString(mockedProduct);

    when(productService.update(any(Product.class)))
        .thenThrow(new EntityNotFoundException(PRODUCT_NOT_FOUND));

    mockMvc
        .perform(
            put(PRODUCT_BY_ID_URL, PRODUCT_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.message").value(PRODUCT_NOT_FOUND));
  }
}
