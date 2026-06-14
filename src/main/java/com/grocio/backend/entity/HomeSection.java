package com.grocio.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "home_sections")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HomeSection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "section_id")
    private Long sectionId;

    @Column(nullable = false, unique = true, length = 50)
    private String code;

    @Column(nullable = false)
    private String title;

    private String subtitle;

    @Column(name = "display_order")
    private Integer displayOrder;

    @Column(name = "layout_type")
    private String layoutType;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @OneToMany(
            mappedBy = "homeSection",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @OrderBy("displayOrder ASC")
    private List<HomeSectionProduct> products = new ArrayList<>();
}