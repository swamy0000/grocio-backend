package com.grocio.backend.service;

import com.grocio.backend.repository.UserRepository;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    // 🟢 ఫోన్ నెంబర్ ఉందో లేదో చెక్ చేసి True / False పంపుతుంది
    public boolean checkUserExists(String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber).isPresent();
    }
}