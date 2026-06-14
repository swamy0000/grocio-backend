package com.grocio.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.grocio.backend.entity.Banner;

@Repository
public interface BannerRepository extends JpaRepository<Banner, Long> {

    @Query("""
            SELECT b
            FROM Banner b
            WHERE b.isActive = true
            AND (b.startDate IS NULL OR b.startDate <= CURRENT_TIMESTAMP)
            AND (b.endDate IS NULL OR b.endDate >= CURRENT_TIMESTAMP)
            ORDER BY b.priority ASC
            """)
    List<Banner> findActiveBanners();

}