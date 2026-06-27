package com.grocio.backend.address.controller;

import com.grocio.backend.address.dto.AddressRequest;
import com.grocio.backend.address.dto.AddressResponse;
import com.grocio.backend.address.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/addresses")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AddressResponse>> getUserAddresses(@PathVariable Long userId) {
        return ResponseEntity.ok(addressService.getUserAddresses(userId));
    }

    @PostMapping("/add")
    public ResponseEntity<Map<String, Object>> addAddress(@RequestBody AddressRequest request) {
        return ResponseEntity.ok(addressService.addAddress(request));
    }

    @PutMapping("/{userId}/set-default/{addressId}")
    public ResponseEntity<Map<String, Object>> setDefaultAddress(@PathVariable Long userId, @PathVariable Long addressId) {
        return ResponseEntity.ok(addressService.setDefaultAddress(userId, addressId));
    }

    @PutMapping("/update/{addressId}")
    public ResponseEntity<Map<String, Object>> updateAddress(@PathVariable Long addressId, @RequestBody AddressRequest request) {
        return ResponseEntity.ok(addressService.updateAddress(addressId, request));
    }

    @DeleteMapping("/delete/{addressId}")
    public ResponseEntity<Map<String, Object>> deleteAddress(@PathVariable Long addressId) {
        return ResponseEntity.ok(addressService.deleteAddress(addressId));
    }
}