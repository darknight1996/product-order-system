package org.example.inventory.util;

import org.example.message.Product;

import java.math.BigDecimal;

public class ProductInitializer {

    public static Product createProduct() {
        return new Product(1L, "product 1", BigDecimal.valueOf(1000));
    }

    public static Product createUpdatedProduct() {
        final Product product = ProductInitializer.createProduct();
        final BigDecimal updatedPrice = product.getPrice().add(BigDecimal.valueOf(1000));

        product.setName("updated name");
        product.setPrice(updatedPrice);

        return product;
    }

}
