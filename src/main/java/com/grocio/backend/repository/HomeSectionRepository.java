package com.grocio.backend.repository;

import com.grocio.backend.entity.HomeSection;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface HomeSectionRepository
        extends JpaRepository<HomeSection, Long> {

    List<HomeSection> findByIsActiveTrueOrderByDisplayOrderAsc();

}