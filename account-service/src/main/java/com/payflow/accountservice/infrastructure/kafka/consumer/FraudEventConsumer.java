package com.payflow.accountservice.infrastructure.kafka.consumer;

import com.payflow.accountservice.application.service.AccountService;
import com.payflow.accountservice.domain.event.FraudDetectedEvent;
import com.payflow.accountservice.infrastructure.kafka.KafkaTopics;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class FraudEventConsumer {
    private final AccountService accountService;

    public FraudEventConsumer(AccountService accountService) {
        this.accountService = accountService;
    }

    @KafkaListener(
            topics = KafkaTopics.FRAUD_DETECTED,
            groupId = "account-service-group",
            properties ={"spring.json.value.default.type=com.payflow.accountservice.domain.event.FraudDetectedEvent"}

    )
    public void handleFraudDetected(FraudDetectedEvent event){
        log.warn("🚨 Fraud event alındı | hesap: {} | sebep: {}",
                event.accountId(), event.reason());
        accountService.suspendAccount(event.accountId(), event.reason());
    }

}
