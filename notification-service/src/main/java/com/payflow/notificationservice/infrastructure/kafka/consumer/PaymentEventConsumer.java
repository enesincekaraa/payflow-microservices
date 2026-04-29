package com.payflow.notificationservice.infrastructure.kafka.consumer;

import com.payflow.notificationservice.application.service.NotificationService;
import com.payflow.notificationservice.domain.event.PaymentNotificationEvent;
import com.payflow.notificationservice.infrastructure.kafka.KafkaTopics;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentEventConsumer {

    private final NotificationService notificationService;

    @KafkaListener(
            topics = KafkaTopics.PAYMENT_COMPLETED,
            groupId = "notification-service-group",
            properties = {
                    "spring.json.value.default.type=com.payflow.notificationservice.domain.event.PaymentNotificationEvent"
            }
    )
    public void handlePaymentCompleted(PaymentNotificationEvent event) {
        log.info("Kafka'dan event alındı: payment-completed | paymentId: {}",
                event.paymentId());
        notificationService.sendPaymentSuccessNotification(event);
    }

    @KafkaListener(
            topics = KafkaTopics.PAYMENT_FAILED,
            groupId = "notification-service-group",
            properties = {
                    "spring.json.value.default.type=com.payflow.notificationservice.domain.event.PaymentNotificationEvent"
            }
    )
    public void handlePaymentFailed(PaymentNotificationEvent event) {
        log.warn("Kafka'dan event alındı: payment-failed | paymentId: {}",
                event.paymentId());
        notificationService.sendPaymentFailedNotification(event);
    }
}