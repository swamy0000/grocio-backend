package com.grocio.backend.authentication.service;

import com.grocio.backend.authentication.dto.CheckPhoneRequest;
import com.grocio.backend.authentication.dto.LoginRequest;
import com.grocio.backend.authentication.dto.LoginResponse;
import com.grocio.backend.authentication.dto.RegisterRequest;
import com.grocio.backend.authentication.dto.RegisterResponse;
import com.grocio.backend.authentication.dto.UpdatePinRequest;
import com.grocio.backend.entity.User;
import com.grocio.backend.repository.UserRepository;
import com.grocio.backend.security.JwtUtil;
import com.grocio.backend.service.UserService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthenticationService {

    private final UserService userService;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public AuthenticationService(UserService userService, UserRepository userRepository, JwtUtil jwtUtil) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    public Map<String, Object> checkPhone(CheckPhoneRequest req) {
        Map<String, Object> response = new HashMap<>();
        try {
            String phone = req.getPhoneNumber();
            boolean exists = userService.checkUserExists(phone);
            response.put("exists", exists);
            response.put("message",
                    exists ? "User found! Proceed to PIN." : "New user! Proceed to Register.");
        } catch (Exception e) {
            e.printStackTrace();
            response.put("exists", false);
            response.put("message", "New user! Proceed to Register.");
        }
        return response;
    }

    public RegisterResponse register(RegisterRequest req) {
        RegisterResponse resp = new RegisterResponse();
        try {
            if (userService.checkUserExists(req.getPhoneNumber())) {
                resp.setSuccess(false);
                resp.setMessage("Phone number already registered!");
                return resp;
            }

            User newUser = new User();
            newUser.setName(req.getName());
            newUser.setPhoneNumber(req.getPhoneNumber());
            newUser.setPin(req.getPin());
            newUser.setEmail(req.getEmail());

            User savedUser = userRepository.save(newUser);

            resp.setSuccess(true);
            resp.setMessage("User registered successfully!");
            resp.setUserId(savedUser.getUserId());
            return resp;
        } catch (Exception e) {
            resp.setSuccess(false);
            resp.setMessage("Registration failed. Try again.");
            return resp;
        }
    }

    public LoginResponse login(LoginRequest req) {
        LoginResponse resp = new LoginResponse();
        try {
            var userOpt = userRepository.findByPhoneNumber(req.getPhoneNumber());
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                if (user.getPin().equals(req.getPin())) {
                    String token = jwtUtil.generateToken(user.getPhoneNumber());
                    resp.setSuccess(true);
                    resp.setMessage("Login successful!");
                    resp.setName(user.getName());
                    resp.setUserId(user.getUserId());
                    resp.setToken(token);
                    return resp;
                } else {
                    resp.setSuccess(false);
                    resp.setMessage("Incorrect PIN! Please try again.");
                    return resp;
                }
            } else {
                resp.setSuccess(false);
                resp.setMessage("User not found!");
                return resp;
            }
        } catch (Exception e) {
            resp.setSuccess(false);
            resp.setMessage("Login failed. Try again.");
            return resp;
        }
    }

    public Map<String, Object> updatePin(UpdatePinRequest req) {
        Map<String, Object> response = new HashMap<>();
        try {
            var userOpt = userRepository.findByPhoneNumber(req.getPhoneNumber());
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                user.setPin(req.getPin());
                userRepository.save(user);
                response.put("success", true);
                response.put("message", "PIN reset successful!");
                return response;
            } else {
                response.put("success", false);
                response.put("message", "User not found!");
                return response;
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Failed to reset PIN. Try again.");
            return response;
        }
    }
}
