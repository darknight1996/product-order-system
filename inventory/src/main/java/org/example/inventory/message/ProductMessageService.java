package org.example.inventory.message;

import org.example.message.ProductEvent;
import org.springframework.stereotype.Service;

@Service
public interface ProductMessageService {

    void productEvent(final ProductEvent productEvent);

}
