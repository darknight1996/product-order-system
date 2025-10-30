package org.example.inventory.repository;

import java.util.Optional;
import org.example.inventory.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {

  Optional<Inventory> findByProductId(final Long productId);

  void deleteByProductId(final Long productId);
}
