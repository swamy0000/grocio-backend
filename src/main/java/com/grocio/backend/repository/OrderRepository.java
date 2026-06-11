package com.grocio.backend.repository;

import com.grocio.backend.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    
    // 🟢 1. ఒక నిర్దిష్ట స్టేటస్ లో ఉన్న ఆర్డర్లను తీసుకురావడం (ఉదా: ORDER_PLACED)
    List<Order> findByStatusOrderByOrderTimeDesc(String status);
    
    // 🟢 2. ఒక యూజర్ (కస్టమర్) పెట్టిన మొత్తం ఆర్డర్ల హిస్టరీ
    List<Order> findByUserIdOrderByOrderTimeDesc(Long userId);
    
    // 🟢 3. డెలివరీ ఏజెంట్ కి అసైన్ అయిన ఆక్టివ్ ఆర్డర్లను చూడటానికి
    List<Order> findByDeliveryPartnerIdAndStatusNot(Long partnerId, String status);
    
    
    List<Order> findByStatus(String status);
    
}