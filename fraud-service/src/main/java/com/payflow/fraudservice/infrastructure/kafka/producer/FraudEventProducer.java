package com.payflow.fraudservice.infrastructure.kafka.producer;

import com.payflow.fraudservice.domain.event.FraudEvent;
import com.payflow.fraudservice.infrastructure.kafka.KafkaTopics;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class FraudEventProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public FraudEventProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendFraudDetected(FraudEvent.FraudDetected event){
        log.warn("🚨 Fraud event gönderiliyor | paymentId: {} | sebep: {}",
                event.paymentId(), event.reason());

        kafkaTemplate.send(
                KafkaTopics.FRAUD_DETECTED,event.paymentId(), event
        );
    }

    public void sendFraudApproved(FraudEvent.FraudApproved event){
        log.info("✅ Fraud approved event gönderiliyor | paymentId: {}",
                event.paymentId());

        kafkaTemplate.send(
                KafkaTopics.FRAUD_APPROVED,event.paymentId(), event
        );
    }
}
