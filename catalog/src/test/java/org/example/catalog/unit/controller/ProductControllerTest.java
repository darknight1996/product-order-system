package org.example.catalog.unit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.example.catalog.controller.ProductController;
import org.example.catalog.entity.Product;
import org.example.catalog.mapper.ProductMapper;
import org.example.catalog.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
public class ProductControllerTest {

    @MockitoBean
    private ProductService productService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ProductMapper productMapper;

    @Test
    public void getAll_shouldReturnAllProducts() throws Exception {
        final List<Product> mockedProducts = createProducts();

        when(productService.getAll()).thenReturn(mockedProducts);

        final ResultActions resultActions = mockMvc.perform(get("/api/v1/product/all"));

        resultActions.andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.size()").value(mockedProducts.size()));

        for (int i = 0; i < mockedProducts.size(); i++) {
            final Product product = mockedProducts.get(i);

            resultActions.andExpect(jsonPath(String.format("$[%d].id", i)).value(product.getId()))
                    .andExpect(jsonPath(String.format("$[%d].name", i)).value(product.getName()))
                    .andExpect(jsonPath(String.format("$[%d].description", i)).value(product.getDescription()))
                    .andExpect(jsonPath(String.format("$[%d].price", i)).value(product.getPrice()));
        }
    }

    @Test
    public void getById_shouldReturnProductById() throws Exception {
        final Product mockedProduct = createProduct();

        when(productService.getById(mockedProduct.getId())).thenReturn(mockedProduct);

        mockMvc.perform(get("/api/v1/product/{id}", mockedProduct.getId())
                        .contentType(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                .andExpect(jsonPath("$.id").value(mockedProduct.getId()))
                .andExpect(jsonPath("$.name").value(mockedProduct.getName()))
                .andExpect(jsonPath("$.description").value(mockedProduct.getDescription()))
                .andExpect(jsonPath("$.price").value(mockedProduct.getPrice()));
    }

    @Test
    public void getById_productNotFound() throws Exception {
        final Long id = 1L;

        when(productService.getById(id)).thenThrow(new EntityNotFoundException("Product not found"));

        mockMvc.perform(get("/api/v1/product/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Product not found"));
    }

    @Test
    public void add_shouldAddProduct() throws Exception {
        final Product mockedProduct = createProduct();
        final String json = objectMapper.writeValueAsString(mockedProduct);

        when(productMapper.productFromProductAddDto(any())).thenReturn(mockedProduct);
        when(productService.add(mockedProduct)).thenReturn(mockedProduct);

        mockMvc.perform(post("/api/v1/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))

                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                .andExpect(jsonPath("$.id").value(mockedProduct.getId()))
                .andExpect(jsonPath("$.name").value(mockedProduct.getName()))
                .andExpect(jsonPath("$.description").value(mockedProduct.getDescription()))
                .andExpect(jsonPath("$.price").value(mockedProduct.getPrice()));
    }

    @Test
    public void add_invalidRequestBody() throws Exception {
        final String invalidJson = "invalid json";

        mockMvc.perform(post("/api/v1/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void delete_shouldDeleteProduct() throws Exception {
        final Long id = 1L;

        mockMvc.perform(delete("/api/v1/product/{id}", id))
                .andExpect(status().isOk());
    }

    @Test
    public void delete_productNotFound() throws Exception {
        final Long id = 1L;

        doThrow(new EntityNotFoundException("Product not found")).when(productService).delete(id);

        mockMvc.perform(delete("/api/v1/product/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    public void update_shouldUpdateProduct() throws Exception {
        final Product mockedProduct = createProduct();
        final String json = objectMapper.writeValueAsString(mockedProduct);

        when(productMapper.productFromProductUpdateDto(any())).thenReturn(mockedProduct);
        when(productService.update(mockedProduct)).thenReturn(mockedProduct);

        mockMvc.perform(put("/api/v1/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))

                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                .andExpect(jsonPath("$.id").value(mockedProduct.getId()))
                .andExpect(jsonPath("$.name").value(mockedProduct.getName()))
                .andExpect(jsonPath("$.description").value(mockedProduct.getDescription()))
                .andExpect(jsonPath("$.price").value(mockedProduct.getPrice()));
    }

    @Test
    public void update_invalidRequestBody() throws Exception {
        final String invalidJson = "invalid json";

        mockMvc.perform(put("/api/v1/product")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(invalidJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void update_productNotFound() throws Exception {
        final Product mockedProduct = createProduct();
        final String json = objectMapper.writeValueAsString(mockedProduct);

        when(productMapper.productFromProductUpdateDto(any())).thenReturn(mockedProduct);
        when(productService.update(mockedProduct)).thenThrow(new EntityNotFoundException("Product not found"));

        mockMvc.perform(put("/api/v1/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Product not found"));;
    }

    private List<Product> createProducts() {
        return List.of(
                new Product(1L,"product 1", "description 1", BigDecimal.valueOf(1000)),
                new Product(2L,"product 2", "description 2", BigDecimal.valueOf(2000))
        );
    }

    private Product createProduct() {
        return new Product(1L, "product", "description", BigDecimal.valueOf(1000));
    }

}
