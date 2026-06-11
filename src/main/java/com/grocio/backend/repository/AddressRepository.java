package com.grocio.backend.repository;

import com.grocio.backend.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
    
    // యూజర్ అడ్రస్‌లు తెస్తుంది (డిఫాల్ట్ వి పైన వచ్చేలా)
    List<Address> findByUserIdOrderByIsDefaultDesc(Long userId);
    
    // పాత డిఫాల్ట్ అడ్రస్ ఏంటో తెలుసుకోవడానికి
    Address findByUserIdAndIsDefaultTrue(Long userId);
}