package com.payflow.accountservice.infrastructure.kafka.consumer;

import com.payflow.accountservice.application.service.AccountService;
import com.payflow.accountservice.domain.event.PaymentInitiatedEvent;
import com.payflow.accountservice.infrastructure.kafka.KafkaTopics;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PaymentEventConsumer {
    private final AccountService accountService;

    public PaymentEventConsumer(AccountService accountService) {
        this.accountService = accountService;
    }

    @KafkaListener(
            topics = KafkaTopics.PAYMENT_INITIATED,
            groupId = "account-service-group"
    )
    public void handlePaymentInitiated(PaymentInitiatedEvent event) {
        log.info("Kafka'dan event alındı: payment-initiated | paymentId: {}",
                event.paymentId());

        accountService.processPayment(event);
    }
}
