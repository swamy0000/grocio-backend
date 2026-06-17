package com.grocio.backend.repository;

import com.grocio.backend.entity.PaymentMode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentModeRepository extends JpaRepository<PaymentMode, Long> {
    List<PaymentMode> findByEnabledTrueOrderByDisplayOrderAsc();
}