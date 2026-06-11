package com.grocio.backend.dto;

import lombok.Data;
import java.util.List;

@Data
public class CartValidationResponseDTO {
    private Boolean canProceed; // 🟢 ట్రూ అయితేనే పేమెంట్ స్క్రీన్ కి వెళ్లనిస్తాం
    private String message;
    private List<String> outOfStockItems; // ఏ ఏ వస్తువులు అయిపోయాయో వాటి పేర్ల లిస్ట్
}