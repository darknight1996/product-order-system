package org.example.catalog.util;

import org.example.catalog.entity.Product;

import java.math.BigDecimal;
import java.util.List;

public class ProductInitializer {

    public static List<Product> createProducts() {
        return List.of(
                new Product(1L, "product 1", "description 1", BigDecimal.valueOf(1000)),
                new Product(2L, "product 2", "description 2", BigDecimal.valueOf(2000))
        );
    }

    public static Product createProduct() {
        return new Product(1L, "product", "description", BigDecimal.valueOf(1000));
    }

    public static Product createUpdatedProduct() {
        final Product updatedProduct = createProduct();

        updatedProduct.setPrice(BigDecimal.valueOf(2000));
        updatedProduct.setName("updated product");
        updatedProduct.setDescription("updated description");

        return updatedProduct;
    }

}
