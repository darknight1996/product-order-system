package org.example.order.service;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public interface CatalogService {

    BigDecimal getProductPrice(Long productId);

}
