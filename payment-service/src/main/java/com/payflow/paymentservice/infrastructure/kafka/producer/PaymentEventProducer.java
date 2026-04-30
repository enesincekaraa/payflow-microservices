package com.payflow.paymentservice.infrastructure.kafka.producer;

import com.payflow.paymentservice.domain.event.PaymentEvent;
import com.payflow.paymentservice.domain.event.PaymentNotificationEvent;
import com.payflow.paymentservice.infrastructure.kafka.KafkaTopics;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PaymentEventProducer {
    private final KafkaTemplate<String,Object> kafkaTemplate;

    public PaymentEventProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendInitiated(PaymentEvent.PaymentInitiated event) {
        log.info("Kafka'ya event gönderiliyor: {} | paymentId: {}",
        KafkaTopics.PAYMENT_INITIATED ,event.paymentId());

        // key olarak paymentId kullan — aynı ödeme her zaman aynı partition'a gider
        kafkaTemplate.send(KafkaTopics.PAYMENT_INITIATED, event.paymentId(),event);
    }


    public void sendNotification(PaymentNotificationEvent event){
        String topic = event.success() ? KafkaTopics.PAYMENT_COMPLETED : KafkaTopics.PAYMENT_FAILED;
        kafkaTemplate.send(topic, event.paymentId(), event);
    }
}
