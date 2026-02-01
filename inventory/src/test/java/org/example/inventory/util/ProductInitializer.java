package org.example.inventory.util;

import java.math.BigDecimal;
import org.example.message.Product;

public class ProductInitializer {

  public static Product createProduct() {
    return new Product(1L, "product 1", BigDecimal.valueOf(1000));
  }

  public static Product createUpdatedProduct() {
    Product product = ProductInitializer.createProduct();

    BigDecimal updatedPrice = product.price().add(BigDecimal.valueOf(1000));
    String updatedName = "updated name";

    product = product.toBuilder().name(updatedName).price(updatedPrice).build();

    return product;
  }
}
