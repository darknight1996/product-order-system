package org.example.catalog.service;

import org.example.catalog.entity.Product;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProductService {

    List<Product> getAll();

    Product getById(final Long id);

    Product add(final Product product);

    void delete(final Long id);

    Product update(final Product product);

}
