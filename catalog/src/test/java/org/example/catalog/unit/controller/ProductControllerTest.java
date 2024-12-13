package org.example.catalog.unit.controller;

import jakarta.persistence.EntityNotFoundException;
import org.example.catalog.controller.ProductController;
import org.example.catalog.entity.Product;
import org.example.catalog.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ProductController.class)
public class ProductControllerTest {

    @MockBean
    private ProductService productService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getAll() throws Exception {
        final List<Product> mockedProducts = getProducts();

        when(productService.getAll()).thenReturn(mockedProducts);

        mockMvc.perform(get("/api/v1/product/all"))
                .andExpect(status().isOk())

                .andExpect(jsonPath("$[0].id").value(mockedProducts.get(0).getId()))
                .andExpect(jsonPath("$[0].name").value(mockedProducts.get(0).getName()))
                .andExpect(jsonPath("$[0].description").value(mockedProducts.get(0).getDescription()))
                .andExpect(jsonPath("$[0].price").value(mockedProducts.get(0).getPrice()))

                .andExpect(jsonPath("$[1].id").value(mockedProducts.get(1).getId()))
                .andExpect(jsonPath("$[1].name").value(mockedProducts.get(1).getName()))
                .andExpect(jsonPath("$[1].description").value(mockedProducts.get(1).getDescription()))
                .andExpect(jsonPath("$[1].price").value(mockedProducts.get(1).getPrice()));
    }

    @Test
    public void getById_productExists() throws Exception {
        final Product mockedProduct = getProduct();

        when(productService.getById(mockedProduct.getId())).thenReturn(mockedProduct);

        mockMvc.perform(get("/api/v1/product/" + mockedProduct.getId()))
                .andExpect(status().isOk())

                .andExpect(jsonPath("$.id").value(mockedProduct.getId()))
                .andExpect(jsonPath("$.name").value(mockedProduct.getName()))
                .andExpect(jsonPath("$.description").value(mockedProduct.getDescription()))
                .andExpect(jsonPath("$.price").value(mockedProduct.getPrice()));
    }

    @Test
    public void getById_productNotFound() throws Exception {
        final Long id = 1L;

        when(productService.getById(id)).thenThrow(new EntityNotFoundException("Product not found"));

        mockMvc.perform(get("/api/v1/product/" + id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Product not found"));
    }

    private List<Product> getProducts() {
        return List.of(
                new Product(1L,"product 1", "description 1", BigDecimal.valueOf(1000)),
                new Product(2L,"product 2", "description 2", BigDecimal.valueOf(2000))
        );
    }

    private Product getProduct() {
        return new Product(1L, "product", "description", BigDecimal.valueOf(1000));
    }

}
