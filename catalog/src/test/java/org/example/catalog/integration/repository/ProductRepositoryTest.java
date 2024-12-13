package org.example.catalog.integration.repository;

import org.example.catalog.entity.Product;
import org.example.catalog.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    public void testSave() {
        final Product product = getProduct();

        final Product savedProduct = productRepository.save(product);

        assertNotNull(savedProduct.getId());
        assertEquals(product.getName(), savedProduct.getName());
        assertEquals(product.getDescription(), savedProduct.getDescription());
        assertEquals(product.getPrice(), savedProduct.getPrice());
    }

    @Test
    public void testFindById() {
        final Product product = productRepository.save(getProduct());

        final Optional<Product> foundProduct = productRepository.findById(product.getId());

        assertTrue(foundProduct.isPresent());
        assertEquals(product.getName(), foundProduct.get().getName());
        assertEquals(product.getDescription(), foundProduct.get().getDescription());
        assertEquals(product.getPrice(), foundProduct.get().getPrice());
    }

    @Test
    public void testFindAll() {
        final List<Product> products = getProducts();

        productRepository.saveAll(products);

        final List<Product> foundProducts = productRepository.findAll();

        assertEquals(products.size(), foundProducts.size());

        assertEquals(products.get(0).getName(), foundProducts.get(0).getName());
        assertEquals(products.get(0).getDescription(), foundProducts.get(0).getDescription());
        assertEquals(products.get(0).getPrice(), foundProducts.get(0).getPrice());

        assertEquals(products.get(1).getName(), foundProducts.get(1).getName());
        assertEquals(products.get(1).getDescription(), foundProducts.get(1).getDescription());
        assertEquals(products.get(1).getPrice(), foundProducts.get(1).getPrice());
    }

    @Test
    public void testDelete() {
        final Product product = getProduct();

        productRepository.save(product);
        productRepository.delete(product);

        final Optional<Product> foundProduct = productRepository.findById(product.getId());

        assertFalse(foundProduct.isPresent());
    }

    private List<Product> getProducts() {
        return List.of(
                new Product("product 1", "description 1", BigDecimal.valueOf(1000)),
                new Product("product 2", "description 2", BigDecimal.valueOf(2000))
        );
    }

    private Product getProduct() {
        return new Product( "product", "description", BigDecimal.valueOf(1000));
    }

}
