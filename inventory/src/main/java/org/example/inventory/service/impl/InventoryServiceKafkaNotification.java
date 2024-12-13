package org.example.inventory.service.impl;

import jakarta.transaction.Transactional;
import org.example.inventory.entity.Inventory;
import org.example.inventory.repository.InventoryRepository;
import org.example.inventory.service.InventoryService;
import org.example.message.ProductEvent;
import org.example.message.model.ActionType;
import org.example.message.model.Product;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InventoryServiceKafkaNotification implements InventoryService {

    private final InventoryRepository inventoryRepository;

    public InventoryServiceKafkaNotification(final InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    @Override
    public List<Inventory> getAll() {
        return inventoryRepository.findAll();
    }

    @KafkaListener(topics = "product-events", groupId = "inventory-consumer-group")
    @Transactional
    public void productDelete(final ProductEvent productEvent) {
        final ActionType actionType = productEvent.getActionType();
        final Product product = productEvent.getProduct();

        switch (actionType) {
            case ADD -> add(product);
            case DELETE -> delete(product);
            case UPDATE -> update(product);
        }
    }

    private void add(final Product product) {
        final Long productId = product.getId();
        final Optional<Inventory> existedInventory = inventoryRepository.findByProductId(productId);

        if (existedInventory.isEmpty()) {
            final Inventory inventory = new Inventory(productId, product.getName(), product.getPrice(), 0);

            inventoryRepository.save(inventory);
        }
    }

    private void delete(final Product product) {
        final Long productId = product.getId();

        inventoryRepository.deleteByProductId(productId);
    }

    private void update(final Product product) {
        final Long productId = product.getId();
        final Optional<Inventory> existedInventory = inventoryRepository.findByProductId(productId);

        if (existedInventory.isPresent()) {
            final Inventory inventory = existedInventory.get();

            inventory.setProductName(product.getName());
            inventory.setProductPrice(product.getPrice());
        }
    }

}
