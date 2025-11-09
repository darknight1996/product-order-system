package org.example.catalog.init;

import jakarta.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.List;
import org.example.catalog.entity.Product;
import org.example.catalog.service.ProductService;
import org.springframework.stereotype.Component;

@Component
public class ProductInitializer {

  private final ProductService productService;

  public ProductInitializer(ProductService productService) {
    this.productService = productService;
  }

  @PostConstruct
  private void init() {
    List<Product> products =
        List.of(
            new Product("Laptop", "High-performance laptop", BigDecimal.valueOf(1500)),
            new Product("Smartphone", "Latest model smartphone", BigDecimal.valueOf(800)),
            new Product("Headphones", "Noise-canceling headphones", BigDecimal.valueOf(200)));

    for (Product product : products) {
      productService.add(product);
    }
  }
}
