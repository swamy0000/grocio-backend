package com.grocio.backend.repository;

import com.grocio.backend.entity.PaymentMode1;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentModeRepository1 extends JpaRepository<PaymentMode1, Long> {
    List<PaymentMode1> findByEnabledTrueOrderByDisplayOrderAsc();
}