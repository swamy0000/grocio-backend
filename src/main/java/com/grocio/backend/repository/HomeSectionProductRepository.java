package com.grocio.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.grocio.backend.entity.HomeSectionProduct;

public interface HomeSectionProductRepository
        extends JpaRepository<HomeSectionProduct, Long> {

}