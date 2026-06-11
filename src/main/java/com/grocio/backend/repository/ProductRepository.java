package com.grocio.backend.repository;

import com.grocio.backend.entity.Product;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    
    @Query(value = "SELECT p.* FROM products p JOIN sub_categories s ON p.sub_category_id = s.sub_category_id WHERE s.category_id = ?1", nativeQuery = true)
    List<Product> findByCategoryId(Long categoryId);
    
    List<Product> findTop5BySubCategoryIdAndProductIdNotAndIsActiveTrue(Long subCategoryId, Long productId);
}
