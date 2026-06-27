package com.grocio.backend.inventory.scheduler;

import com.grocio.backend.inventory.service.InventoryReservationService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InventoryReservationExpiryScheduler {

    private static final Logger log = LoggerFactory.getLogger(InventoryReservationExpiryScheduler.class);
    private final InventoryReservationService inventoryReservationService;

    @Scheduled(cron = "0 * * * * *")
    public void expireReservations() {
        try {
            int releasedCount = inventoryReservationService.expireReservations().size();
            if (releasedCount > 0) {
                log.info("Released {} expired inventory reservations.", releasedCount);
            }
        } catch (Exception exception) {
            log.error("Failed to expire inventory reservations.", exception);
        }
    }
}
