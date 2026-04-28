package com.payflow.accountservice.infrastructure.kafka.producer;

import com.payflow.accountservice.domain.event.AccountDebitedEvent;
import com.payflow.accountservice.infrastructure.kafka.KafkaTopics;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AccountEventProducer {
    private final KafkaTemplate<String,Object> kafkaTemplate;
    public AccountEventProducer(KafkaTemplate<String,Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }
    public void sendAccountDebited(AccountDebitedEvent event) {
        log.info("Kafka'ya event gönderiliyor: account-debited | paymentId: {}",
                event.paymentId());
        kafkaTemplate.send(KafkaTopics.ACCOUNT_DEBITED,event.paymentId(), event);
    }
}
