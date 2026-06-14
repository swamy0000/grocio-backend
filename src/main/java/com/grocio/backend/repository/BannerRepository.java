package com.grocio.backend.repository;

import com.grocio.backend.entity.Banner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BannerRepository extends JpaRepository<Banner, Long> {

    List<Banner> findByIsActiveTrueAndStartDateLessThanEqualAndEndDateGreaterThanEqualOrderByPriorityAsc(
            LocalDateTime now1,
            LocalDateTime now2
    );
}