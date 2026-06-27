package com.grocio.backend.address.repository;

import com.grocio.backend.address.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
    
    List<Address> findByUserIdOrderByIsDefaultDesc(Long userId);
    
    Address findByUserIdAndIsDefaultTrue(Long userId);
}
