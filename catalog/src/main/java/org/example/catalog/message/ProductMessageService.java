package org.example.catalog.message;

import org.example.catalog.entity.Product;
import org.springframework.stereotype.Service;

@Service
public interface ProductMessageService {

  void sendAdd(Product product);

  void sendDelete(Product product);

  void sendUpdate(Product product);
}
