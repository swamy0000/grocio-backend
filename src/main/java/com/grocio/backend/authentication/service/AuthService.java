package com.grocio.backend.authentication.service;

import com.grocio.backend.authentication.dto.request.CheckPhoneRequest;
import com.grocio.backend.authentication.dto.request.LoginRequest;
import com.grocio.backend.authentication.dto.request.RegisterRequest;
import com.grocio.backend.authentication.dto.request.ResetPinRequest;
import com.grocio.backend.authentication.dto.response.CheckPhoneResponse;
import com.grocio.backend.authentication.dto.response.LoginResponse;
import com.grocio.backend.authentication.dto.response.RegisterResponse;

public interface AuthService {

    CheckPhoneResponse checkPhone(CheckPhoneRequest request);

    RegisterResponse register(RegisterRequest request);

    LoginResponse login(LoginRequest request);

    void resetPin(ResetPinRequest request);
}
