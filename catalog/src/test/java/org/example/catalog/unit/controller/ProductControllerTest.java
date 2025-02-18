package org.example.catalog.unit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.example.catalog.controller.ProductController;
import org.example.catalog.entity.Product;
import org.example.catalog.mapper.ProductMapper;
import org.example.catalog.service.ProductService;
import org.example.catalog.util.ProductInitializer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    private static final String PRODUCT_URL = "/api/v1/product";
    private static final String PRODUCT_URL_WITH_ID = PRODUCT_URL + "/{id}";
    private static final String PRODUCT_URL_ALL = PRODUCT_URL + "/all";
    private static final String INVALID_JSON = "Invalid JSON";
    private static final String PRODUCT_NOT_FOUND = "Product not found";
    private static final Long PRODUCT_ID = 1L;

    @MockitoBean
    private ProductService productService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ProductMapper productMapper;

    @Test
    void getAll_shouldReturnAllProducts() throws Exception {
        final List<Product> mockedProducts = ProductInitializer.createProducts();

        when(productService.getAll()).thenReturn(mockedProducts);

        final ResultActions resultActions = mockMvc.perform(get(PRODUCT_URL_ALL));

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
    void getById_shouldReturnProductById() throws Exception {
        final Product mockedProduct = ProductInitializer.createProduct();

        when(productService.getById(mockedProduct.getId())).thenReturn(mockedProduct);

        mockMvc.perform(get(PRODUCT_URL_WITH_ID, mockedProduct.getId())
                        .contentType(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                .andExpect(jsonPath("$.id").value(mockedProduct.getId()))
                .andExpect(jsonPath("$.name").value(mockedProduct.getName()))
                .andExpect(jsonPath("$.description").value(mockedProduct.getDescription()))
                .andExpect(jsonPath("$.price").value(mockedProduct.getPrice()));
    }

    @Test
    void getById_productNotFound() throws Exception {
        when(productService.getById(PRODUCT_ID)).thenThrow(new EntityNotFoundException(PRODUCT_NOT_FOUND));

        mockMvc.perform(get(PRODUCT_URL_WITH_ID, PRODUCT_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(PRODUCT_NOT_FOUND));
    }

    @Test
    void add_shouldAddProduct() throws Exception {
        final Product mockedProduct = ProductInitializer.createProduct();
        final String json = objectMapper.writeValueAsString(mockedProduct);

        when(productMapper.productFromProductAddDto(any())).thenReturn(mockedProduct);
        when(productService.add(mockedProduct)).thenReturn(mockedProduct);

        mockMvc.perform(post(PRODUCT_URL)
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
    void add_invalidRequestBody() throws Exception {
        mockMvc.perform(post(PRODUCT_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(INVALID_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void delete_shouldDeleteProduct() throws Exception {
        mockMvc.perform(delete(PRODUCT_URL_WITH_ID, PRODUCT_ID))
                .andExpect(status().isOk());
    }

    @Test
    void delete_productNotFound() throws Exception {
        doThrow(new EntityNotFoundException(PRODUCT_NOT_FOUND)).when(productService).delete(PRODUCT_ID);

        mockMvc.perform(delete(PRODUCT_URL_WITH_ID, PRODUCT_ID))
                .andExpect(status().isNotFound());
    }

    @Test
    void update_shouldUpdateProduct() throws Exception {
        final Product mockedProduct = ProductInitializer.createProduct();
        final String json = objectMapper.writeValueAsString(mockedProduct);

        when(productMapper.productFromProductUpdateDto(any())).thenReturn(mockedProduct);
        when(productService.update(mockedProduct)).thenReturn(mockedProduct);

        mockMvc.perform(put(PRODUCT_URL)
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
    void update_invalidRequestBody() throws Exception {
        mockMvc.perform(put(PRODUCT_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(INVALID_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void update_productNotFound() throws Exception {
        final Product mockedProduct = ProductInitializer.createProduct();
        final String json = objectMapper.writeValueAsString(mockedProduct);

        when(productMapper.productFromProductUpdateDto(any())).thenReturn(mockedProduct);
        when(productService.update(mockedProduct)).thenThrow(new EntityNotFoundException(PRODUCT_NOT_FOUND));

        mockMvc.perform(put(PRODUCT_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(PRODUCT_NOT_FOUND));
    }

}
