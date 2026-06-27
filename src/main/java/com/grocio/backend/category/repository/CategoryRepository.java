package com.grocio.backend.category.repository;

import com.grocio.backend.category.entity.Category;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

       List<Category> findAllByOrderByIdAsc();
}
