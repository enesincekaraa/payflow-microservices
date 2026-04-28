package com.payflow.paymentservice.infrastructure.kafka.consumer;

import com.payflow.paymentservice.application.service.PaymentService;
import com.payflow.paymentservice.domain.event.AccountDebitedEvent;
import com.payflow.paymentservice.infrastructure.kafka.KafkaTopics;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AccountEventConsumer {
    private final PaymentService paymentService;

    public AccountEventConsumer(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @KafkaListener(
            topics = KafkaTopics.ACCOUNT_DEBITED,
            groupId = "payment-service-group"
    )
    public void handleAccountDebited(AccountDebitedEvent event){
        log.info("Kafka'dan event alındı: account-debited | paymentId: {}",
                event.paymentId());

        if (event.success()) {
            paymentService.completePayment(event.paymentId());
        } else {
            paymentService.failPayment(event.paymentId(), event.failureReason());
        }
    }
}
