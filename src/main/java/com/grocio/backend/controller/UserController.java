package com.grocio.backend.controller;

import com.grocio.backend.entity.User;
import com.grocio.backend.repository.UserRepository;
import com.grocio.backend.security.JwtUtil;
import com.grocio.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*") // 🟢 ఫ్లట్టర్ నుండి వచ్చే కాల్స్ బ్లాక్ అవ్వకుండా చూసుకుంటుంది
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    // 🟢 మన ఫస్ట్ API Endpoint: http://localhost:8089/api/users/check-phone
    @PostMapping("/check-phone")
    public ResponseEntity<Map<String, Object>> checkPhone(@RequestBody Map<String, String> request) {
        String phone = request.get("phoneNumber");

        boolean exists = userService.checkUserExists(phone);

        // ఫ్లట్టర్ కి రెస్పాన్స్ పంపడానికి
        Map<String, Object> response = new HashMap<>();
        response.put("exists", exists);
        response.put("message", exists ? "User found! Proceed to PIN." : "New user! Proceed to Register.");

        return ResponseEntity.ok(response);
    }

    // 🟢 మన సెకండ్ API Endpoint: http://localhost:8089/api/users/register
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> registerUser(@RequestBody User newUser) {
        Map<String, Object> response = new HashMap<>();

        try {
            // ఆ ఫోన్ నెంబర్ ఇప్పటికే ఉందేమో మళ్ళీ ఒకసారి చెక్ చేస్తున్నాం (Safety కోసం)
            if (userService.checkUserExists(newUser.getPhoneNumber())) {
                response.put("success", false);
                response.put("message", "Phone number already registered!");
                return ResponseEntity.badRequest().body(response);
            }

            // కొత్త యూజర్‌ని డేటాబేస్ లో సేవ్ చేస్తున్నాం
            User savedUser = userRepository.save(newUser);

            response.put("success", true);
            response.put("message", "User registered successfully!");
            response.put("userId", savedUser.getUserId());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Registration failed. Try again.");
            return ResponseEntity.internalServerError().body(response);
        }
    }

    // 🟢 మన థర్డ్ API Endpoint: http://localhost:8089/api/users/login
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> loginUser(@RequestBody Map<String, String> request) {
        String phone = request.get("phoneNumber");
        String pin = request.get("pin");

        Map<String, Object> response = new HashMap<>();

        try {
            // నెంబర్ తో యూజర్ ఉన్నాడో లేదో డేటాబేస్ నుండి తెస్తున్నాం
            var userOpt = userRepository.findByPhoneNumber(phone);

            if (userOpt.isPresent()) {
                User user = userOpt.get();
                // ఎంటర్ చేసిన పిన్, డేటాబేస్ లోని పిన్ మ్యాచ్ అయ్యాయా?
                if (user.getPin().equals(pin)) {
                    // 🟢 ఇక్కడ టోకెన్ జనరేట్ చేస్తున్నాం
                    String token = jwtUtil.generateToken(user.getPhoneNumber());

                    response.put("success", true);
                    response.put("message", "Login successful!");
                    response.put("name", user.getName());
                    response.put("userId", user.getUserId());

                    // 🟢 ఫ్లట్టర్ కోసం టోకెన్ పంపుతున్నాం
                    response.put("token", token);

                    return ResponseEntity.ok(response);
                } else {
                    response.put("success", false);
                    response.put("message", "Incorrect PIN! Please try again.");
                    return ResponseEntity.badRequest().body(response);
                }
            } else {
                response.put("success", false);
                response.put("message", "User not found!");
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Login failed. Try again.");
            return ResponseEntity.internalServerError().body(response);
        }
    }

    // 🟢 కొత్త API Endpoint: Forgot PIN కోసం (Update PIN) ->
    // http://localhost:8089/api/users/update-pin
    @PostMapping("/update-pin")
    public ResponseEntity<Map<String, Object>> updatePin(@RequestBody Map<String, String> request) {
        String phone = request.get("phoneNumber");
        String newPin = request.get("pin");

        Map<String, Object> response = new HashMap<>();

        try {
            var userOpt = userRepository.findByPhoneNumber(phone);

            if (userOpt.isPresent()) {
                User user = userOpt.get();
                user.setPin(newPin); // కొత్త PIN సెట్ చేస్తున్నాం
                userRepository.save(user);

                response.put("success", true);
                response.put("message", "PIN reset successful!");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "User not found!");
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Failed to reset PIN. Try again.");
            return ResponseEntity.internalServerError().body(response);
        }
    }

    // 🟢 ప్రొఫైల్ అప్‌డేట్ API
    @PutMapping("/update/{id}")
    public ResponseEntity<Map<String, Object>> updateUser(@PathVariable Long id, @RequestBody User updatedData) {
        Map<String, Object> response = new HashMap<>();
        try {
            User existingUser = userRepository.findById(id).orElse(null);
            if (existingUser != null) {

                // కొత్త డేటాతో పాత డేటాని మారుస్తున్నాం
                existingUser.setName(updatedData.getName());
                existingUser.setPhoneNumber(updatedData.getPhoneNumber());
                if (updatedData.getEmail() != null) {
                    existingUser.setEmail(updatedData.getEmail());
                }
                if (updatedData.getProfileImageUrl() != null) {
                    existingUser.setProfileImageUrl(updatedData.getProfileImageUrl());
                }

                userRepository.save(existingUser);

                response.put("success", true);
                response.put("message", "Profile updated successfully");
                response.put("user", existingUser);
                return ResponseEntity.ok(response);
            }
            response.put("success", false);
            response.put("message", "User not found");
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error updating profile");
            return ResponseEntity.internalServerError().body(response);
        }
    }

    // 🟢 యూజర్ డీటెయిల్స్ గెట్ చేసుకునే API
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserDetails(@PathVariable Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.notFound().build();
    }
}