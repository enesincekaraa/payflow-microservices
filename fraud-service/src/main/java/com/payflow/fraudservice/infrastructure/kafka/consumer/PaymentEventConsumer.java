package com.payflow.fraudservice.infrastructure.kafka.consumer;

import com.payflow.fraudservice.application.service.FraudDetectionService;
import com.payflow.fraudservice.domain.event.PaymentInitiatedEvent;
import com.payflow.fraudservice.infrastructure.kafka.KafkaTopics;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PaymentEventConsumer {

    private final FraudDetectionService fraudDetectionService;

    public PaymentEventConsumer(FraudDetectionService fraudDetectionService) {
        this.fraudDetectionService = fraudDetectionService;
    }

    @KafkaListener(
            topics = KafkaTopics.PAYMENT_INITIATED,
            groupId = "fraud-service-group"
    )
    public void handlePaymentInitiated(PaymentInitiatedEvent event) {
        log.info("Kafka'dan event alındı: payment-initiated | paymentId: {}",
                event.paymentId());
        fraudDetectionService.analyze(event);
    }
}
