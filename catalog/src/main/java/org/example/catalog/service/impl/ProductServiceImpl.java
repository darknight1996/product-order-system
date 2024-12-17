package org.example.catalog.service.impl;

import jakarta.persistence.EntityNotFoundException;
import org.example.catalog.entity.Product;
import org.example.catalog.message.ProductMessageService;
import org.example.catalog.repository.ProductRepository;
import org.example.catalog.service.ProductService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    private final ProductMessageService productMessageService;

    public ProductServiceImpl(final ProductRepository productRepository,
                              final ProductMessageService productMessageService) {
        this.productRepository = productRepository;
        this.productMessageService = productMessageService;
    }

    @Override
    public List<Product> getAll() {
        return productRepository.findAll();
    }

    @Override
    public Product getById(final Long id) {
        final Optional<Product> productOptional = productRepository.findById(id);

        return productOptional.orElseThrow(() -> new EntityNotFoundException("Product not found"));
    }

    @Override
    public Product add(final Product product) {
        final Product savedProduct = productRepository.save(product);

        productMessageService.sendAdd(savedProduct);

        return savedProduct;
    }

    @Override
    public void delete(final Long id) {
        final Optional<Product> existedProduct = productRepository.findById(id);

        if (existedProduct.isPresent()) {
            productRepository.deleteById(id);

            productMessageService.sendDelete(existedProduct.get());
        } else {
            throw new EntityNotFoundException("Product not found");
        }
    }

    @Override
    public Product update(final Product product) {
        final Product savedProduct = productRepository.save(product);

        productMessageService.sendUpdate(savedProduct);

        return savedProduct;
    }

}
