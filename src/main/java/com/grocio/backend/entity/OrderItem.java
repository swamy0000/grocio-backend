package com.grocio.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "order_items")
@Data
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id") // 🟢 మీ ఇమేజ్ లో ప్రైమరీ కీ పేరు 'id' అని ఉంది, అలాగే పెట్టాను
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    @JsonIgnore
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    // 🟢 మీ ఇమేజ్ లో ఉన్నట్లుగా మ్యాప్ చేశాను
    @Column(name = "price_at_that_time", nullable = false)
    private Double priceAtThatTime;

    @Column(nullable = false)
    private Integer quantity;
}