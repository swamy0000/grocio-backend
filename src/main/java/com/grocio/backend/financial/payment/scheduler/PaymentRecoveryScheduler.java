package com.grocio.backend.financial.payment.scheduler;

import com.grocio.backend.financial.payment.internal.PaymentRecoveryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentRecoveryScheduler {

    private final PaymentRecoveryService paymentRecoveryService;

    @Scheduled(cron = "0 */10 * * * *")
    public void runPaymentRecovery() {
        log.info("Starting payment recovery scheduler");

        var summary = paymentRecoveryService.recoverSuccessfulPayments();

        log.info(
                "Payment recovery completed: paymentsReviewed={}, ordersRecovered={}, reservationsRecovered={}, couponsRecovered={}, cartsRecovered={}, failures={}",
                summary.paymentsReviewed(), summary.ordersRecovered(), summary.reservationsRecovered(),
                summary.couponsRecovered(), summary.cartsRecovered(), summary.failures());
    }
}
