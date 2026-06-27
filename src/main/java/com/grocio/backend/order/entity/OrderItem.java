package com.grocio.backend.order.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.grocio.backend.product.entity.Product;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Table(name = "order_items")
@Data
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    @JsonIgnore
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "price_at_that_time", nullable = false)
    private BigDecimal priceAtThatTime;

    @Column(nullable = false)
    private Integer quantity;
}
