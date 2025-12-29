package org.example.catalog.unit.util;

import java.math.BigDecimal;
import java.util.List;
import lombok.experimental.UtilityClass;
import org.example.catalog.entity.Product;

@UtilityClass
public class ProductInitializer {

  public List<Product> createProducts() {
    return List.of(
        new Product(1L, "product 1", "description 1", BigDecimal.valueOf(1000)),
        new Product(2L, "product 2", "description 2", BigDecimal.valueOf(2000)));
  }

  public Product createProduct() {
    return new Product(1L, "product", "description", BigDecimal.valueOf(1000));
  }

  public Product createUpdatedProduct() {
    Product updatedProduct = createProduct();
    BigDecimal updatedPrice = updatedProduct.getPrice().add(BigDecimal.valueOf(2000));

    updatedProduct.setPrice(updatedPrice);
    updatedProduct.setName("updated product");
    updatedProduct.setDescription("updated description");

    return updatedProduct;
  }
}
