package com.grocio.backend.authentication.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.grocio.backend.authentication.dto.CheckPhoneRequest;
import com.grocio.backend.authentication.dto.LoginRequest;
import com.grocio.backend.authentication.dto.LoginResponse;
import com.grocio.backend.authentication.dto.RegisterRequest;
import com.grocio.backend.authentication.dto.RegisterResponse;
import com.grocio.backend.authentication.dto.UpdatePinRequest;
import com.grocio.backend.authentication.service.AuthenticationService;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/check-phone")
    public ResponseEntity<Map<String, Object>> checkPhone(@RequestBody CheckPhoneRequest request) {
        var response = authenticationService.checkPhone(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> registerUser(@RequestBody RegisterRequest newUser) {
        RegisterResponse resp = authenticationService.register(newUser);
        if (!resp.isSuccess() && "Phone number already registered!".equals(resp.getMessage())) {
            return ResponseEntity.badRequest().body(resp);
        }
        if (!resp.isSuccess()) {
            return ResponseEntity.internalServerError().body(resp);
        }
        return ResponseEntity.ok(resp);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginUser(@RequestBody LoginRequest request) {
        LoginResponse resp = authenticationService.login(request);
        if (!resp.isSuccess()) {
            String msg = resp.getMessage();
            if ("Incorrect PIN! Please try again.".equals(msg) || "User not found!".equals(msg)) {
                return ResponseEntity.badRequest().body(resp);
            }
            return ResponseEntity.internalServerError().body(resp);
        }
        return ResponseEntity.ok(resp);
    }

    @PostMapping("/update-pin")
    public ResponseEntity<Map<String, Object>> updatePin(@RequestBody UpdatePinRequest request) {
        Map<String, Object> resp = authenticationService.updatePin(request);
        boolean success = Boolean.TRUE.equals(resp.get("success"));
        if (success) {
            return ResponseEntity.ok(resp);
        }
        String msg = (String) resp.get("message");
        if ("User not found!".equals(msg)) {
            return ResponseEntity.badRequest().body(resp);
        }
        return ResponseEntity.internalServerError().body(resp);
    }
}
