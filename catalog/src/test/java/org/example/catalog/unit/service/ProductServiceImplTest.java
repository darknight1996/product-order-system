package org.example.catalog.unit.service;

import jakarta.persistence.EntityNotFoundException;
import org.example.catalog.entity.Product;
import org.example.catalog.message.ProductMessageService;
import org.example.catalog.repository.ProductRepository;
import org.example.catalog.service.impl.ProductServiceImpl;
import org.example.catalog.util.ProductInitializer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMessageService productMessageService;

    @InjectMocks
    private ProductServiceImpl cut;

    @Test
    void getAll_shouldReturnAllProducts() {
        final List<Product> products = ProductInitializer.createProducts();

        when(productRepository.findAll()).thenReturn(products);

        final List<Product> result = cut.getAll();

        assertEquals(2, products.size());

        assertEquals(products.get(0), result.get(0));
        assertEquals(products.get(1), result.get(1));
    }

    @Test
    void getById_shouldReturnProductById() {
        final Product product = ProductInitializer.createProduct();

        when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));

        final Product result = cut.getById(product.getId());

        assertEquals(product, result);
    }

    @Test
    void getById_productNotFound() {
        final Long id = 1L;

        when(productRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> cut.getById(id));
    }

    @Test
    void add_shouldAddProduct() {
        final Product product = ProductInitializer.createProduct();

        when(productRepository.save(product)).thenReturn(product);

        final Product result = cut.add(product);

        assertEquals(product, result);

        verify(productRepository, times(1)).save(product);
        verify(productMessageService, times(1)).sendAdd(product);
    }

    @Test
    void delete_shouldDeleteProduct() {
        final Product product = ProductInitializer.createProduct();

        when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));

        cut.delete(product.getId());

        verify(productRepository, times(1)).deleteById(product.getId());
        verify(productMessageService, times(1)).sendDelete(product);
    }

    @Test
    void delete_productNotFound() {
        final Long id = 1L;

        when(productRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> cut.delete(id));

        verify(productRepository, never()).deleteById(any());
        verify(productMessageService, never()).sendDelete(any());
    }

    @Test
    void update_shouldUpdateProduct() {
        final Product existedProduct = ProductInitializer.createProduct();
        final Product updatedProduct = ProductInitializer.createUpdatedProduct();

        when(productRepository.findById(existedProduct.getId())).thenReturn(Optional.of(existedProduct));
        when(productRepository.save(existedProduct)).thenReturn(updatedProduct);

        final Product result = cut.update(updatedProduct);

        assertNotNull(result);
        assertEquals(updatedProduct, result);

        assertEquals(existedProduct.getName(), updatedProduct.getName());
        assertEquals(existedProduct.getPrice(), updatedProduct.getPrice());
        assertEquals(existedProduct.getDescription(), updatedProduct.getDescription());

        verify(productRepository, times(1)).save(existedProduct);
        verify(productMessageService, times(1)).sendUpdate(updatedProduct);
    }

    @Test
    void update_productNotFound() {
        final Product product = ProductInitializer.createProduct();

        when(productRepository.findById(product.getId())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> cut.update(product));

        verify(productRepository, never()).save(any());
        verify(productMessageService, never()).sendUpdate(any());
    }

}
