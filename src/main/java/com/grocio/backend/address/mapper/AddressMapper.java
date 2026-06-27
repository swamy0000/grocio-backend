package com.grocio.backend.address.mapper;

import com.grocio.backend.address.dto.AddressRequest;
import com.grocio.backend.address.dto.AddressResponse;
import com.grocio.backend.address.entity.Address;

public class AddressMapper {

    private AddressMapper() {
    }

    public static AddressResponse toResponse(Address address) {

        AddressResponse dto = new AddressResponse();

        dto.setAddressId(address.getAddressId());
        dto.setUserId(address.getUserId());

        dto.setTitle(address.getTitle());

        dto.setFlatNo(address.getFlatNo());
        dto.setFormattedAddress(address.getFormattedAddress());

        dto.setCity(address.getCity());
        dto.setState(address.getState());

        dto.setLandmark(address.getLandmark());
        dto.setPincode(address.getPincode());

        dto.setLatitude(address.getLatitude());
        dto.setLongitude(address.getLongitude());

        dto.setReceiverName(address.getReceiverName());
        dto.setReceiverPhone(address.getReceiverPhone());

        dto.setIsDefault(address.getIsDefault());

        return dto;
    }

    public static Address toEntity(AddressResponse response) {
        if (response == null) {
            return null;
        }

        Address address = new Address();
        address.setAddressId(response.getAddressId());
        address.setUserId(response.getUserId());
        address.setTitle(response.getTitle());
        address.setFlatNo(response.getFlatNo());
        address.setFormattedAddress(response.getFormattedAddress());
        address.setCity(response.getCity());
        address.setState(response.getState());
        address.setLandmark(response.getLandmark());
        address.setPincode(response.getPincode());
        address.setLatitude(response.getLatitude());
        address.setLongitude(response.getLongitude());
        address.setReceiverName(response.getReceiverName());
        address.setReceiverPhone(response.getReceiverPhone());
        address.setIsDefault(response.getIsDefault());
        return address;
    }

    public static Address toEntity(AddressRequest req) {
        Address a = new Address();

        a.setUserId(req.getUserId());
        a.setTitle(req.getTitle());

        a.setFlatNo(req.getFlatNo());
        a.setFormattedAddress(req.getFormattedAddress());

        a.setCity(req.getCity());
        a.setState(req.getState());
        a.setPlaceId(req.getPlaceId());
        a.setLatitude(req.getLatitude());
        a.setLongitude(req.getLongitude());

        a.setLandmark(req.getLandmark());
        a.setPincode(req.getPincode());

        a.setReceiverName(req.getReceiverName());
        a.setReceiverPhone(req.getReceiverPhone());

        a.setIsDefault(req.getIsDefault());

        return a;
    }
}