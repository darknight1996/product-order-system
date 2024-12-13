package org.example.catalog.service.impl;

import jakarta.persistence.EntityNotFoundException;
import org.example.catalog.entity.Product;
import org.example.catalog.repository.ProductRepository;
import org.example.catalog.service.ProductService;
import org.example.message.ProductEvent;
import org.example.message.model.ActionType;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceKafkaNotification implements ProductService {

    private final ProductRepository productRepository;

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public ProductServiceKafkaNotification(final ProductRepository productRepository,
                                           final KafkaTemplate<String, Object> kafkaTemplate) {
        this.productRepository = productRepository;
        this.kafkaTemplate = kafkaTemplate;

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

        sendProductAdd(savedProduct);

        return savedProduct;
    }

    @Override
    public void delete(final Long id) {
        final Optional<Product> existedProduct = productRepository.findById(id);

        if (existedProduct.isPresent()) {
            productRepository.deleteById(id);

            sendProductDelete(existedProduct.get());
        } else {
            throw new EntityNotFoundException("Product not found");
        }
    }

    @Override
    public Product update(final Product product) {
        final Product savedProduct = productRepository.save(product);

        sendProductUpdate(savedProduct);

        return productRepository.save(product);
    }

    private void sendProductAdd(final Product product) {
        final ProductEvent productEvent = new ProductEvent(product, ActionType.ADD);

        sendMessage(productEvent);
    }

    private void sendProductDelete(final Product product) {
        final ProductEvent productEvent = new ProductEvent(product, ActionType.DELETE);

        sendMessage(productEvent);
    }

    private void sendProductUpdate(final Product product) {
        final ProductEvent productEvent = new ProductEvent(product, ActionType.UPDATE);

        sendMessage(productEvent);
    }

    private void sendMessage(final ProductEvent productEvent) {
        kafkaTemplate.send("product-events", productEvent);
    }

}
