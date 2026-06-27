package com.grocio.backend.address.service;

import com.grocio.backend.address.dto.AddressRequest;
import com.grocio.backend.address.dto.AddressResponse;
import com.grocio.backend.address.entity.Address;
import com.grocio.backend.address.exception.AddressException;
import com.grocio.backend.address.mapper.AddressMapper;
import com.grocio.backend.address.repository.AddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;

    public List<AddressResponse> getUserAddresses(Long userId) {
        return addressRepository
                .findByUserIdOrderByIsDefaultDesc(userId)
                .stream()
                .map(AddressMapper::toResponse)
                .toList();
    }

    public boolean isAddressOwnedByUser(Long addressId, Long userId) {
        return addressRepository.findById(addressId)
                .map(address -> address.getUserId() != null && address.getUserId().equals(userId))
                .orElse(false);
    }

    public AddressResponse getValidatedUserAddress(Long addressId, Long userId) {
        Address address = getAddressById(addressId);
        if (address == null || !Objects.equals(address.getUserId(), userId)) {
            throw new AddressException("Invalid delivery address for user");
        }
        return AddressMapper.toResponse(address);
    }

    public Address getAddressById(Long addressId) {
        return addressRepository.findById(addressId).orElse(null);
    }

    public Map<String, Object> addAddress(AddressRequest req) {
        Map<String, Object> response = new HashMap<>();
        try {
            if (req.getIsDefault() != null && req.getIsDefault()) {
                Address oldDefault = addressRepository.findByUserIdAndIsDefaultTrue(req.getUserId());
                if (oldDefault != null) {
                    oldDefault.setIsDefault(false);
                    addressRepository.save(oldDefault);
                }
            } else {
                long count = addressRepository.count();
                if (count == 0) {
                    req.setIsDefault(true);
                } else {
                    req.setIsDefault(false);
                }
            }

            Address toSave = AddressMapper.toEntity(req);
            addressRepository.save(toSave);
            response.put("success", true);
            response.put("message", "Address saved successfully!");
            return response;
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Failed to save address");
            return response;
        }
    }

    public Map<String, Object> setDefaultAddress(Long userId, Long addressId) {
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
            return response;
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error updating default address");
            return response;
        }
    }

    public Map<String, Object> updateAddress(Long addressId, AddressRequest updatedReq) {
        Map<String, Object> response = new HashMap<>();
        try {
            Address existingAddress = addressRepository.findById(addressId).orElse(null);
            if (existingAddress != null) {
                existingAddress.setTitle(updatedReq.getTitle());
                existingAddress.setReceiverName(updatedReq.getReceiverName());
                existingAddress.setReceiverPhone(updatedReq.getReceiverPhone());

                existingAddress.setFlatNo(updatedReq.getFlatNo());
                existingAddress.setFormattedAddress(updatedReq.getFormattedAddress());
                existingAddress.setCity(updatedReq.getCity());
                existingAddress.setState(updatedReq.getState());
                existingAddress.setPlaceId(updatedReq.getPlaceId());
                existingAddress.setLatitude(updatedReq.getLatitude());
                existingAddress.setLongitude(updatedReq.getLongitude());

                existingAddress.setLandmark(updatedReq.getLandmark());
                existingAddress.setPincode(updatedReq.getPincode());

                addressRepository.save(existingAddress);

                response.put("success", true);
                response.put("message", "Address updated successfully!");
                return response;
            }
            response.put("success", false);
            response.put("message", "Address not found");
            return response;
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Failed to update address");
            return response;
        }
    }

    public Map<String, Object> deleteAddress(Long addressId) {
        Map<String, Object> response = new HashMap<>();
        try {
            if (addressRepository.existsById(addressId)) {
                addressRepository.deleteById(addressId);
                response.put("success", true);
                response.put("message", "Address deleted successfully!");
                return response;
            }
            response.put("success", false);
            response.put("message", "Address not found");
            return response;
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Failed to delete address");
            return response;
        }
    }
}
