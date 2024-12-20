package org.example.inventory.service;

import org.example.inventory.entity.Inventory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface InventoryService {

    List<Inventory> getAll();

    Inventory getById(final Long id);

    Inventory add(final Inventory inventory);

    void delete(final Long id);

    Inventory updateQuantity(final Long id, final Integer quantity);

}
