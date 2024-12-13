package org.example.inventory.service;

import org.example.inventory.entity.Inventory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface InventoryService {

    List<Inventory> getAll();
}
