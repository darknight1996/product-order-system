package org.example.inventory.service.impl;

import jakarta.persistence.EntityNotFoundException;
import org.example.inventory.entity.Inventory;
import org.example.inventory.repository.InventoryRepository;
import org.example.inventory.service.InventoryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;

    public InventoryServiceImpl(final InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    @Override
    public List<Inventory> getAll() {
        return inventoryRepository.findAll();
    }

    @Override
    public Inventory getById(final Long id) {
        return getExistedInventory(id);
    }

    @Override
    public Inventory add(final Inventory inventory) {
        return inventoryRepository.save(inventory);
    }

    @Override
    public void delete(final Long id) {
        final Inventory existedInventory = getExistedInventory(id);

        inventoryRepository.delete(existedInventory);
    }

    @Override
    public Inventory updateQuantity(final Long id, final Integer quantity) {
        final Inventory existedInventory = getExistedInventory(id);

        existedInventory.setQuantity(quantity);

        return inventoryRepository.save(existedInventory);
    }

    private Inventory getExistedInventory(final Long id) {
        return inventoryRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Inventory not found"));
    }

}
