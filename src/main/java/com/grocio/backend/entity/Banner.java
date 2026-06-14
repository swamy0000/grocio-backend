package com.grocio.backend.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "banners")
public class Banner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "banner_id")
    private Long bannerId;

    private String title;

    private String subtitle;

    @Column(name = "button_text")
    private String buttonText;

    @Column(name = "image_url", columnDefinition = "TEXT")
    private String imageUrl;

    @Column(name = "redirect_type")
    private String redirectType;

    @Column(name = "redirect_id")
    private Long redirectId;

    private Integer priority = 1;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
}