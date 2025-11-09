package org.example.catalog.service;

import java.util.List;
import org.example.catalog.entity.Product;
import org.springframework.stereotype.Service;

@Service
public interface ProductService {

  List<Product> getAll();

  Product getById(Long id);

  Product add(Product product);

  void delete(Long id);

  Product update(Product product);
}
