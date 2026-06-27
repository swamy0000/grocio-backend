package com.grocio.backend.profile.service;

import com.grocio.backend.entity.User;
import com.grocio.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class ProfileService {

    private final UserRepository userRepository;

    public ProfileService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUserDetails(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public User updateUser(Long id, User updatedData) {
        User existingUser = userRepository.findById(id).orElse(null);
        if (existingUser != null) {
            existingUser.setName(updatedData.getName());
            existingUser.setPhoneNumber(updatedData.getPhoneNumber());
            if (updatedData.getEmail() != null) {
                existingUser.setEmail(updatedData.getEmail());
            }
            if (updatedData.getProfileImageUrl() != null) {
                existingUser.setProfileImageUrl(updatedData.getProfileImageUrl());
            }
            return userRepository.save(existingUser);
        }
        return null;
    }
}
