package com.grocio.backend.repository;

import com.grocio.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // 🟢 డేటాబేస్ లో ఫోన్ నెంబర్ ఉందో లేదో వెతికే మ్యాజిక్ క్వెరీ ఇది
    Optional<User> findByPhoneNumber(String phoneNumber);
}