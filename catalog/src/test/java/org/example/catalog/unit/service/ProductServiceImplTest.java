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
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    private static final Product PRODUCT = ProductInitializer.createProduct();
    private static final Product UPDATED_PRODUCT = ProductInitializer.createUpdatedProduct();
    private static final List<Product> PRODUCTS = ProductInitializer.createProducts();

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMessageService productMessageService;

    @InjectMocks
    private ProductServiceImpl cut;

    @Test
    void getAll_shouldReturnAllProducts() {
        when(productRepository.findAll()).thenReturn(PRODUCTS);

        final List<Product> result = cut.getAll();

        assertEquals(PRODUCTS.size(), result.size());

        assertEquals(PRODUCTS, result);

        verify(productRepository, times(1)).findAll();
    }

    @Test
    void getById_shouldReturnProductById() {
        when(productRepository.findById(PRODUCT.getId())).thenReturn(Optional.of(PRODUCT));

        final Product result = cut.getById(PRODUCT.getId());

        assertEquals(PRODUCT, result);

        verify(productRepository, times(1)).findById(PRODUCT.getId());
    }

    @Test
    void getById_productNotFound() {
        when(productRepository.findById(PRODUCT.getId())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> cut.getById(PRODUCT.getId()));

        verify(productRepository, times(1)).findById(PRODUCT.getId());
    }

    @Test
    void add_shouldAddProduct() {
        when(productRepository.save(PRODUCT)).thenReturn(PRODUCT);

        final Product result = cut.add(PRODUCT);

        assertEquals(PRODUCT, result);

        verify(productRepository, times(1)).save(PRODUCT);
        verify(productMessageService, times(1)).sendAdd(PRODUCT);
    }

    @Test
    void delete_shouldDeleteProduct() {
        when(productRepository.findById(PRODUCT.getId())).thenReturn(Optional.of(PRODUCT));

        cut.delete(PRODUCT.getId());

        verify(productRepository, times(1)).deleteById(PRODUCT.getId());
        verify(productMessageService, times(1)).sendDelete(PRODUCT);
    }

    @Test
    void delete_productNotFound() {
        when(productRepository.findById(PRODUCT.getId())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> cut.delete(PRODUCT.getId()));

        verify(productRepository, never()).deleteById(PRODUCT.getId());
        verify(productMessageService, never()).sendDelete(PRODUCT);
    }

    @Test
    void update_shouldUpdateProduct() {
        when(productRepository.findById(PRODUCT.getId())).thenReturn(Optional.of(PRODUCT));
        when(productRepository.save(PRODUCT)).thenReturn(UPDATED_PRODUCT);

        final Product result = cut.update(PRODUCT);

        assertNotNull(result);
        assertEquals(UPDATED_PRODUCT, result);

        verify(productRepository, times(1)).save(PRODUCT);
        verify(productMessageService, times(1)).sendUpdate(UPDATED_PRODUCT);
    }

    @Test
    void update_productNotFound() {
        when(productRepository.findById(PRODUCT.getId())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> cut.update(PRODUCT));

        verify(productRepository, never()).save(PRODUCT);
        verify(productMessageService, never()).sendUpdate(PRODUCT);
    }

}
