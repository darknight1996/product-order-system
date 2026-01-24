package org.example.order.client;

import org.example.order.dto.external.ProductResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "catalog-client", url = "${catalog.url}")
public interface CatalogClient {

  @GetMapping("/api/v1/products/{id}")
  ProductResponseDTO getProductById(@PathVariable Long id);
}
