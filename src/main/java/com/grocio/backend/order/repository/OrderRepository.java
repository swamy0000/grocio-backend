package com.grocio.backend.order.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.grocio.backend.order.entity.Order;
import com.grocio.backend.order.lifecycle.OrderStatus;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

        @EntityGraph(attributePaths = { "items", "items.product" })
        List<Order> findByStatusOrderByOrderTimeDesc(OrderStatus status);

        @EntityGraph(attributePaths = { "items", "items.product" })
        List<Order> findByUserIdAndStatusOrderByOrderTimeDesc(Long userId, OrderStatus status);

        @EntityGraph(attributePaths = { "items", "items.product" })
        List<Order> findByUserIdOrderByOrderTimeDesc(Long userId);

        @Override
        @EntityGraph(attributePaths = { "items", "items.product" })
        Optional<Order> findById(Long id);

        List<Order> findByDeliveryPartnerIdAndStatusNot(Long partnerId, OrderStatus status);

        List<Order> findByStatus(OrderStatus status);

        Page<Order> findByUserIdOrderByOrderTimeDesc(Long userId, Pageable pageable);

        Page<Order> findByUserIdAndStatusOrderByOrderTimeDesc(Long userId, OrderStatus status, Pageable pageable);

        Page<Order> findByUserIdAndOrderTimeBetweenOrderByOrderTimeDesc(Long userId, LocalDateTime dateFrom,
                        LocalDateTime dateTo, Pageable pageable);

        Page<Order> findByUserIdAndStatusAndOrderTimeBetweenOrderByOrderTimeDesc(Long userId, OrderStatus status,
                        LocalDateTime dateFrom, LocalDateTime dateTo, Pageable pageable);
}
