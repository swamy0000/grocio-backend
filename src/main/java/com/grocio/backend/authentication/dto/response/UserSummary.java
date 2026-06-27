package com.grocio.backend.authentication.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class UserSummary {
    private Long userId;
    private String name;
    private String phoneNumber;
    private String profileImage;
    private BigDecimal walletBalance;
}
