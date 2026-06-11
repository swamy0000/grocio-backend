package com.grocio.backend.service;

import com.grocio.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // 🟢 ఫోన్ నెంబర్ ఉందో లేదో చెక్ చేసి True / False పంపుతుంది
    public boolean checkUserExists(String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber).isPresent();
    }
}