package org.example.catalog.message;

import org.example.catalog.entity.Product;
import org.springframework.stereotype.Service;

@Service
public interface ProductMessageService {

    void sendAdd(final Product product);

    void sendDelete(final Product product);

    void sendUpdate(final Product product);

}
