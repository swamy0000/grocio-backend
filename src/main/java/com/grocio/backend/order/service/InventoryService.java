package com.grocio.backend.order.service;

import com.grocio.backend.product.entity.Product;
import com.grocio.backend.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * InventoryService handles all inventory operations including stock management
 */
@Service
@RequiredArgsConstructor
public class InventoryService {

    private final ProductRepository productRepository;

    /**
     * Deduct stock from a product
     * 
     * @param productId ID of the product
     * @param quantity  Quantity to deduct
     * @throws RuntimeException if product not found or insufficient stock
     */
    @Transactional(rollbackFor = Exception.class)
    public void deductStock(Long productId, Integer quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + productId));

        int currentStock = product.getStockQuantity() != null ? product.getStockQuantity() : 0;

        if (currentStock < quantity) {
            throw new RuntimeException("Out of stock for item: " + product.getName() +
                    " (Only " + currentStock + " left)");
        }

        product.setStockQuantity(currentStock - quantity);
        productRepository.save(product);
    }

    /**
     * Get current stock quantity for a product
     * 
     * @param productId ID of the product
     * @return Current stock quantity
     */
    public int getStockQuantity(Long productId) {
        return productRepository.findById(productId)
                .map(p -> p.getStockQuantity() != null ? p.getStockQuantity() : 0)
                .orElse(0);
    }

    /**
     * Check if sufficient stock is available
     * 
     * @param productId        ID of the product
     * @param requiredQuantity Required quantity
     * @return true if stock is available, false otherwise
     */
    public boolean isStockAvailable(Long productId, Integer requiredQuantity) {
        return getStockQuantity(productId) >= requiredQuantity;
    }

    /**
     * Release stock back to inventory (increase quantity)
     * 
     * @param productId product id
     * @param quantity  quantity to add back
     */
    @Transactional(rollbackFor = Exception.class)
    public void releaseStock(Long productId, Integer quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + productId));

        int currentStock = product.getStockQuantity() != null ? product.getStockQuantity() : 0;
        product.setStockQuantity(currentStock + (quantity != null ? quantity : 0));
        productRepository.save(product);
    }
}
