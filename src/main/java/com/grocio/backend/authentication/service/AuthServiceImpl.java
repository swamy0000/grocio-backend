package com.grocio.backend.authentication.service;

import com.grocio.backend.entity.User;
import com.grocio.backend.repository.UserRepository;
import com.grocio.backend.security.JwtUtil;
import com.grocio.backend.authentication.dto.request.CheckPhoneRequest;
import com.grocio.backend.authentication.dto.request.LoginRequest;
import com.grocio.backend.authentication.dto.request.RegisterRequest;
import com.grocio.backend.authentication.dto.request.ResetPinRequest;
import com.grocio.backend.authentication.dto.response.CheckPhoneResponse;
import com.grocio.backend.authentication.dto.response.LoginResponse;
import com.grocio.backend.authentication.dto.response.RegisterResponse;
import com.grocio.backend.authentication.mapper.AuthMapper;
import com.grocio.backend.authentication.validator.AuthValidator;
import com.grocio.backend.common.exception.ValidationException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class AuthServiceImpl implements AuthService {

    private static final String TOKEN_TYPE = "Bearer";

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final AuthValidator authValidator;
    private final AuthMapper authMapper;

    public AuthServiceImpl(UserRepository userRepository,
                           JwtUtil jwtUtil,
                           AuthValidator authValidator,
                           AuthMapper authMapper) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.authValidator = authValidator;
        this.authMapper = authMapper;
    }

    @Override
    public CheckPhoneResponse checkPhone(CheckPhoneRequest request) {
        authValidator.validateCheckPhone(request);

        boolean exists = userRepository.findByPhoneNumber(request.getPhoneNumber()).isPresent();
        String message = exists ? "User found! Proceed to PIN." : "New user! Proceed to Register.";

        return new CheckPhoneResponse(exists, message);
    }

    @Override
    public RegisterResponse register(RegisterRequest request) {
        authValidator.validateRegister(request);

        if (userRepository.findByPhoneNumber(request.getPhoneNumber()).isPresent()) {
            throw new ValidationException("Phone number already registered.");
        }

        User newUser = new User();
        newUser.setName(request.getName());
        newUser.setPhoneNumber(request.getPhoneNumber());
        newUser.setPin(request.getPin());
        newUser.setEmail(request.getEmail());
        newUser.setProfileImageUrl(request.getProfileImageUrl());
        newUser.setWalletBalance(BigDecimal.ZERO);
        newUser.setStreakCount(0);

        User savedUser = userRepository.save(newUser);
        String token = jwtUtil.generateToken(savedUser.getPhoneNumber());

        return buildRegisterResponse(savedUser, token);
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        authValidator.validateLogin(request);

        User user = userRepository.findByPhoneNumber(request.getPhoneNumber())
                .orElseThrow(() -> new ValidationException("Invalid phone number or PIN."));

        if (!user.getPin().equals(request.getPin())) {
            throw new ValidationException("Invalid phone number or PIN.");
        }

        String token = jwtUtil.generateToken(user.getPhoneNumber());
        return buildLoginResponse(user, token);
    }

    @Override
    public void resetPin(ResetPinRequest request) {
        authValidator.validateResetPin(request);

        User user = userRepository.findByPhoneNumber(request.getPhoneNumber())
                .orElseThrow(() -> new ValidationException("User not found."));

        user.setPin(request.getPin());
        userRepository.save(user);
    }

    private LoginResponse buildLoginResponse(User user, String token) {
        return LoginResponse.builder()
                .accessToken(token)
                .tokenType(TOKEN_TYPE)
                .expiresIn(jwtUtil.getTokenValidityInSeconds())
                .user(authMapper.toUserSummary(user))
                .build();
    }

    private RegisterResponse buildRegisterResponse(User user, String token) {
        return RegisterResponse.builder()
                .accessToken(token)
                .tokenType(TOKEN_TYPE)
                .expiresIn(jwtUtil.getTokenValidityInSeconds())
                .user(authMapper.toUserSummary(user))
                .build();
    }
}
