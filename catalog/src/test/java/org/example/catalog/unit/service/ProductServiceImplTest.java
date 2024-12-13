package org.example.catalog.unit.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import jakarta.persistence.EntityNotFoundException;
import org.example.catalog.entity.Product;
import org.example.catalog.message.ProductMessageService;
import org.example.catalog.repository.ProductRepository;
import org.example.catalog.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
    public void getAll() {
        final List<Product> mockedProducts = getProducts();

        when(productRepository.findAll()).thenReturn(mockedProducts);

        final List<Product> products = cut.getAll();

        assertEquals(2, products.size());

        assertEquals(mockedProducts.get(0).getId(), products.get(0).getId());
        assertEquals(mockedProducts.get(1).getId(), products.get(1).getId());

        assertEquals(mockedProducts.get(0).getName(), products.get(0).getName());
        assertEquals(mockedProducts.get(1).getName(), products.get(1).getName());

        assertEquals(mockedProducts.get(0).getDescription(), products.get(0).getDescription());
        assertEquals(mockedProducts.get(1).getDescription(), products.get(1).getDescription());

        assertEquals(mockedProducts.get(0).getPrice(), products.get(0).getPrice());
        assertEquals(mockedProducts.get(1).getPrice(), products.get(1).getPrice());

    }

    @Test
    public void getById_productExists() {
        final Product mockedProduct = getProduct();

        when(productRepository.findById(mockedProduct.getId())).thenReturn(Optional.of(mockedProduct));

        final Product product = cut.getById(mockedProduct.getId());

        assertEquals(mockedProduct.getId(), product.getId());
        assertEquals(mockedProduct.getName(), product.getName());
        assertEquals(mockedProduct.getDescription(), product.getDescription());
        assertEquals(mockedProduct.getPrice(), product.getPrice());
    }

    @Test
    public void getById_productNotFound() {
        final Long id = 1L;

        when(productRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> cut.getById(id));
    }

    @Test
    public void add() throws JsonProcessingException {
        final Product mockedProduct = getProduct();

        when(productRepository.save(mockedProduct)).thenReturn(mockedProduct);

        final Product product = cut.add(mockedProduct);

        assertEquals(mockedProduct.getId(), product.getId());
        assertEquals(mockedProduct.getName(), product.getName());
        assertEquals(mockedProduct.getDescription(), product.getDescription());
        assertEquals(mockedProduct.getPrice(), product.getPrice());

        verify(productRepository, times(1)).save(mockedProduct);
        verify(productMessageService,times(1)).sendAdd(mockedProduct);
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

}
