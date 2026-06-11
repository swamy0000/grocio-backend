package com.grocio.backend.controller;

import com.grocio.backend.entity.Address;
import com.grocio.backend.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/addresses")
@CrossOrigin(origins = "*")
public class AddressController {

    @Autowired
    private AddressRepository addressRepository;

    // 🟢 1. యూజర్ అడ్రస్‌లన్నీ చూసే API
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Address>> getUserAddresses(@PathVariable Long userId) {
        List<Address> addresses = addressRepository.findByUserIdOrderByIsDefaultDesc(userId);
        return ResponseEntity.ok(addresses);
    }

    // 🟢 2. కొత్త అడ్రస్ యాడ్ చేసే API
    @PostMapping("/add")
    public ResponseEntity<Map<String, Object>> addAddress(@RequestBody Address newAddress) {
        Map<String, Object> response = new HashMap<>();
        try {
            if (newAddress.getIsDefault() != null && newAddress.getIsDefault()) {
                Address oldDefault = addressRepository.findByUserIdAndIsDefaultTrue(newAddress.getUserId());
                if (oldDefault != null) {
                    oldDefault.setIsDefault(false);
                    addressRepository.save(oldDefault);
                }
            } else {
                long count = addressRepository.count();
                if (count == 0) {
                    newAddress.setIsDefault(true);
                } else {
                    newAddress.setIsDefault(false);
                }
            }

            addressRepository.save(newAddress);
            response.put("success", true);
            response.put("message", "Address saved successfully!");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Failed to save address");
            return ResponseEntity.internalServerError().body(response);
        }
    }

    // 🟢 3. ఒక అడ్రస్‌ని డిఫాల్ట్ గా సెట్ చేసే API
    @PutMapping("/{userId}/set-default/{addressId}")
    public ResponseEntity<Map<String, Object>> setDefaultAddress(@PathVariable Long userId,
            @PathVariable Long addressId) {
        Map<String, Object> response = new HashMap<>();
        try {
            Address oldDefault = addressRepository.findByUserIdAndIsDefaultTrue(userId);
            if (oldDefault != null) {
                oldDefault.setIsDefault(false);
                addressRepository.save(oldDefault);
            }

            Address newDefault = addressRepository.findById(addressId).orElse(null);
            if (newDefault != null && newDefault.getUserId().equals(userId)) {
                newDefault.setIsDefault(true);
                addressRepository.save(newDefault);
            }

            response.put("success", true);
            response.put("message", "Default address updated!");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error updating default address");
            return ResponseEntity.internalServerError().body(response);
        }
    }

    // 🟢 4. పాత అడ్రస్‌ని ఎడిట్ (Update) చేసే API
    @PutMapping("/update/{addressId}")
    public ResponseEntity<Map<String, Object>> updateAddress(@PathVariable Long addressId, @RequestBody Address updatedAddress) {
        Map<String, Object> response = new HashMap<>();
        try {
            Address existingAddress = addressRepository.findById(addressId).orElse(null);
            if (existingAddress != null) {
                existingAddress.setTitle(updatedAddress.getTitle());
                existingAddress.setReceiverName(updatedAddress.getReceiverName());
                existingAddress.setReceiverPhone(updatedAddress.getReceiverPhone());
                existingAddress.setFullAddress(updatedAddress.getFullAddress());
                existingAddress.setLandmark(updatedAddress.getLandmark());
                existingAddress.setPincode(updatedAddress.getPincode()); // పింకోడ్ కూడా సింక్ చేశాం
                
                addressRepository.save(existingAddress);
                
                response.put("success", true);
                response.put("message", "Address updated successfully!");
                return ResponseEntity.ok(response);
            }
            response.put("success", false);
            response.put("message", "Address not found");
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Failed to update address");
            return ResponseEntity.internalServerError().body(response);
        }
    }

    // 🟢 5. అడ్రస్‌ను డెలిట్ (Delete) చేసే కొత్త API (ఇది ఇందాక మిస్ అయింది)
    @DeleteMapping("/delete/{addressId}")
    public ResponseEntity<Map<String, Object>> deleteAddress(@PathVariable Long addressId) {
        Map<String, Object> response = new HashMap<>();
        try {
            if (addressRepository.existsById(addressId)) {
                addressRepository.deleteById(addressId);
                response.put("success", true);
                response.put("message", "Address deleted successfully!");
                return ResponseEntity.ok(response);
            }
            response.put("success", false);
            response.put("message", "Address not found");
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Failed to delete address");
            return ResponseEntity.internalServerError().body(response);
        }
    }
}