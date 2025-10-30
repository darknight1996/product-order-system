package org.example.order.service;

import java.math.BigDecimal;
import org.springframework.stereotype.Service;

@Service
public interface CatalogService {

  BigDecimal getProductPrice(Long productId);
}
