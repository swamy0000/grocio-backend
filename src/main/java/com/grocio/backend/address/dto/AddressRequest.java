package com.grocio.backend.address.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AddressRequest {

    private Long userId;

    private String title;

    private String flatNo;
    private String formattedAddress;

    private String city;
    private String state;

    private String placeId;

    private BigDecimal latitude;
    private BigDecimal longitude;

    private String landmark;

    private String receiverName;
    private String receiverPhone;

    private Boolean isDefault;

    private String pincode;
}