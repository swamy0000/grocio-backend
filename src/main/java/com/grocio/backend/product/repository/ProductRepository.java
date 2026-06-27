package com.grocio.backend.product.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.grocio.backend.entity.SubCategory;
import com.grocio.backend.product.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findTop5BySubCategoryAndProductIdNotAndIsActiveTrue(
            SubCategory subCategory,
            Long productId);

    List<Product> findBySubCategory_IdAndIsActiveTrue(Long subCategoryId);

    List<Product> findTop10ByIsActiveTrueOrderByProductIdDesc();

    @Query("""
                SELECT p
                FROM com.grocio.backend.product.entity.Product p
                WHERE p.subCategory.category.id = :categoryId
                AND p.isActive = true
                ORDER BY p.productId
            """)
    List<Product> findByCategoryId(Long categoryId);
}
