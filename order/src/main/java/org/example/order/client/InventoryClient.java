package org.example.order.client;

import org.example.order.dto.OrderAddDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "inventory-client", url = "${inventory.url}")
public interface InventoryClient {

  @PostMapping("/api/v1/inventory/adjust")
  void adjustInventory(@RequestBody OrderAddDTO orderAddDTO);
}
