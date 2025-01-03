package org.example.order.service;

import org.example.order.dto.OrderAddDTO;
import org.springframework.stereotype.Service;

@Service
public interface InventoryClient {

    void adjustInventory(OrderAddDTO orderAddDTO);

}
