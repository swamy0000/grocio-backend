package com.grocio.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "products")
@Data // Lombok వాడుతుంటే.. లేకపోతే Getters/Setters రాయండి
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long productId;

    @Column(name = "sub_category_id")
    private Long subCategoryId;

    private String name;
    private Double price;
    
    @Column(name = "old_price")
    private Double oldPrice;
    
    private String unit;
    
    @Column(name = "image_url")
    private String imageUrl;
    
    private String badge;
    
    @Column(name = "stock_quantity")
    private Integer stockQuantity;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "shelf_life")
    private String shelfLife;
    
    private String origin;
    private Double rating; // ఫ్రంట్-ఎండ్ కోసం
    
    @Column(name = "is_active")
    private Boolean isActive = true;
}