package org.example.catalog.service.impl;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import org.example.catalog.entity.Product;
import org.example.catalog.message.ProductMessageService;
import org.example.catalog.repository.ProductRepository;
import org.example.catalog.service.ProductService;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService {

  private final ProductRepository productRepository;

  private final ProductMessageService productMessageService;

  public ProductServiceImpl(
      ProductRepository productRepository, ProductMessageService productMessageService) {
    this.productRepository = productRepository;
    this.productMessageService = productMessageService;
  }

  @Override
  public List<Product> getAll() {
    return productRepository.findAll();
  }

  @Override
  public Product getById(Long id) {
    return getExistedProduct(id);
  }

  @Override
  public Product add(Product product) {
    Product savedProduct = productRepository.save(product);

    productMessageService.sendAdd(savedProduct);

    return savedProduct;
  }

  @Override
  public void delete(Long id) {
    final Product existedProduct = getExistedProduct(id);

    productRepository.deleteById(id);

    productMessageService.sendDelete(existedProduct);
  }

  @Override
  public Product update(Product product) {
    Product existedProduct = getExistedProduct(product.getId());

    existedProduct.setName(product.getName());
    existedProduct.setDescription(product.getDescription());
    existedProduct.setPrice(product.getPrice());

    Product savedProduct = productRepository.save(existedProduct);

    productMessageService.sendUpdate(savedProduct);

    return savedProduct;
  }

  private Product getExistedProduct(Long id) {
    return productRepository
        .findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Product not found"));
  }
}
