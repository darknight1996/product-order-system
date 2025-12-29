package org.example.catalog.integration.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.example.catalog.entity.Product;
import org.example.catalog.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class ProductRepositoryTest {

  @Autowired private ProductRepository productRepository;

  @Test
  void testSave() {
    Product product = getProduct();

    Product savedProduct = productRepository.save(product);

    assertNotNull(savedProduct.getId());
    assertEquals(product, savedProduct);
  }

  @Test
  void testFindById() {
    Product product = productRepository.save(getProduct());

    Optional<Product> foundProduct = productRepository.findById(product.getId());

    assertTrue(foundProduct.isPresent());
    assertEquals(product, foundProduct.get());
  }

  @Test
  void testFindAll() {
    List<Product> products = getProducts();

    productRepository.saveAll(products);

    List<Product> foundProducts = productRepository.findAll();

    assertEquals(products.size(), foundProducts.size());
    assertEquals(products.get(0), foundProducts.get(0));
    assertEquals(products.get(1), foundProducts.get(1));
  }

  @Test
  void testDelete() {
    Product product = getProduct();

    productRepository.save(product);
    productRepository.delete(product);

    Optional<Product> foundProduct = productRepository.findById(product.getId());

    assertFalse(foundProduct.isPresent());
  }

  private List<Product> getProducts() {
    return List.of(
        Product.builder()
            .name("product 1")
            .description("description 1")
            .price(BigDecimal.valueOf(1000))
            .build(),
        Product.builder()
            .name("product 2")
            .description("description 2")
            .price(BigDecimal.valueOf(2000))
            .build());
  }

  private Product getProduct() {
    return Product.builder()
        .name("product")
        .description("description")
        .price(BigDecimal.valueOf(1000))
        .build();
  }
}
