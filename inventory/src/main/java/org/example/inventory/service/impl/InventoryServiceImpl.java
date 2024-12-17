package org.example.inventory.service.impl;

import jakarta.persistence.EntityNotFoundException;
import org.example.inventory.entity.Inventory;
import org.example.inventory.repository.InventoryRepository;
import org.example.inventory.service.InventoryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
        final Optional<Inventory> inventoryOptional = inventoryRepository.findById(id);

        return inventoryOptional.orElseThrow(() -> new EntityNotFoundException("Inventory not found"));
    }

    @Override
    public Inventory add(final Inventory inventory) {
        return inventoryRepository.save(inventory);
    }

    @Override
    public void delete(final Long id) {
        final Optional<Inventory> inventoryOptional = inventoryRepository.findById(id);

        if (inventoryOptional.isPresent()) {
            inventoryRepository.delete(inventoryOptional.get());
        } else {
            throw new EntityNotFoundException("Inventory not found");
        }
    }

    @Override
    public Inventory update(final Inventory inventory) {
        return inventoryRepository.save(inventory);
    }

}
