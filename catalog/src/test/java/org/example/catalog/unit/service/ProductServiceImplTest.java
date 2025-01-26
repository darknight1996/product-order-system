package org.example.catalog.unit.service;

import jakarta.persistence.EntityNotFoundException;
import org.example.catalog.entity.Product;
import org.example.catalog.message.ProductMessageService;
import org.example.catalog.repository.ProductRepository;
import org.example.catalog.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMessageService productMessageService;

    @InjectMocks
    private ProductServiceImpl cut;

    @Test
    public void getAll_shouldReturnAllProducts() {
        final List<Product> products = getProducts();

        when(productRepository.findAll()).thenReturn(products);

        final List<Product> result = cut.getAll();

        assertEquals(2, products.size());

        assertEquals(products.get(0), result.get(0));
        assertEquals(products.get(1), result.get(1));
    }

    @Test
    public void getById_shouldReturnProductById() {
        final Product product = getProduct();

        when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));

        final Product result = cut.getById(product.getId());

        assertEquals(product, result);
    }

    @Test
    public void getById_productNotFound() {
        final Long id = 1L;

        when(productRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> cut.getById(id));
    }

    @Test
    public void add_shouldAddProduct() {
        final Product product = getProduct();

        when(productRepository.save(product)).thenReturn(product);

        final Product result = cut.add(product);

        assertEquals(product, result);

        verify(productRepository, times(1)).save(product);
        verify(productMessageService,times(1)).sendAdd(product);
    }

    @Test
    public void delete_shouldDeleteProduct() {
        final Product product = getProduct();

        when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));

        cut.delete(product.getId());

        verify(productRepository, times(1)).deleteById(product.getId());
        verify(productMessageService, times(1)).sendDelete(product);
    }

    @Test
    public void delete_productNotFound() {
        final Long id = 1L;

        when(productRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> cut.delete(id));

        verify(productRepository, never()).deleteById(any());
        verify(productMessageService, never()).sendDelete(any());
    }

    @Test
    public void update_shouldUpdateProduct() {
        final Product existedProduct = getProduct();
        final Product updatedProduct = getUpdatedProduct();

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
    public void update_productNotFound() {
        final Product product = getProduct();

        when(productRepository.findById(product.getId())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> cut.update(product));

        verify(productRepository, never()).save(any());
        verify(productMessageService, never()).sendUpdate(any());
    }

    private List<Product> getProducts() {
        return List.of(
                new Product(1L,"product 1", "description 1", BigDecimal.valueOf(1000)),
                new Product(2L,"product 2", "description 2", BigDecimal.valueOf(2000))
        );
    }

    private Product getProduct() {
        return new Product(1L, "product", "description", BigDecimal.valueOf(1000));
    }

    private Product getUpdatedProduct() {
        final Product updatedProduct = getProduct();

        updatedProduct.setName("updated product");
        updatedProduct.setDescription("updated description");

        return updatedProduct;
    }

}
