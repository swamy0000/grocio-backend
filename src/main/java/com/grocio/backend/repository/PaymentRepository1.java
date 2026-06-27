package com.grocio.backend.repository;

import com.grocio.backend.entity.Payment1;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository1 extends JpaRepository<Payment1, Long> {
}