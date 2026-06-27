package com.grocio.backend.inventory.repository;

import com.grocio.backend.inventory.entity.Inventory;
import com.grocio.backend.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    Optional<Inventory> findByProduct(Product product);

    Optional<Inventory> findByProduct_ProductId(Long productId);
}
