package org.example.catalog.service.impl;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.catalog.entity.Product;
import org.example.catalog.mapper.ProductMapper;
import org.example.catalog.message.ProductMessageService;
import org.example.catalog.repository.ProductRepository;
import org.example.catalog.service.ProductService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductServiceImpl implements ProductService {

  private final ProductRepository productRepository;
  private final ProductMessageService productMessageService;
  private final ProductMapper productMapper;

  @Override
  public List<Product> getAll() {
    return productRepository.findAll();
  }

  @Override
  public Product getById(Long id) {
    return getExistedProduct(id);
  }

  @Override
  @Transactional
  public Product add(Product product) {
    log.info("Creating new product: {}", product.getName());

    Product savedProduct = productRepository.save(product);

    productMessageService.sendAdd(savedProduct);

    return savedProduct;
  }

  @Override
  @Transactional
  public void delete(Long id) {
    log.info("Deleting product with id: {}", id);

    Product existedProduct = getExistedProduct(id);

    productRepository.delete(existedProduct);

    productMessageService.sendDelete(existedProduct);
  }

  @Override
  @Transactional
  public Product update(Product product) {
    Long id = product.getId();

    log.info("Updating product with id: {}", id);

    Product existedProduct = getExistedProduct(id);

    productMapper.updateProductFromProduct(product, existedProduct);

    Product savedProduct = productRepository.save(existedProduct);

    productMessageService.sendUpdate(savedProduct);

    return savedProduct;
  }

  @Override
  public long count() {
    return productRepository.count();
  }

  private Product getExistedProduct(Long id) {
    return productRepository
        .findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + id));
  }
}
