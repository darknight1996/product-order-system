package org.example.catalog.init;

import jakarta.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.catalog.entity.Product;
import org.example.catalog.service.ProductService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductInitializer {

  private final ProductService productService;

  @PostConstruct
  private void init() {
    if (productService.count() == 0) {
      addDefaultProducts();
    }
  }

  private void addDefaultProducts() {
    List<Product> products =
        List.of(
            Product.builder()
                .name("Laptop")
                .description("High-performance laptop")
                .price(BigDecimal.valueOf(1500))
                .build(),
            Product.builder()
                .name("Smartphone")
                .description("Latest model smartphone")
                .price(BigDecimal.valueOf(800))
                .build(),
            Product.builder()
                .name("Headphones")
                .description("Noise-canceling headphones")
                .price(BigDecimal.valueOf(200))
                .build());

    for (Product product : products) {
      productService.add(product);
    }
  }
}
