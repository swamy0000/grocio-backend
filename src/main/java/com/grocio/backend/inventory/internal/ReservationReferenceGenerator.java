package com.grocio.backend.inventory.internal;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Component
public class ReservationReferenceGenerator {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

    public String generateReference() {
        String date = LocalDate.now().format(DATE_FORMATTER);
        String suffix = UUID.randomUUID().toString().replaceAll("[-]", "").substring(0, 8).toUpperCase();
        return String.format("RES-%s-%s", date, suffix);
    }
}
