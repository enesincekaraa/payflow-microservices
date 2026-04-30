package com.payflow.fraudservice.application.service;

import com.payflow.fraudservice.domain.event.FraudEvent;
import com.payflow.fraudservice.domain.event.PaymentInitiatedEvent;
import com.payflow.fraudservice.infrastructure.kafka.producer.FraudEventProducer;
import com.payflow.fraudservice.infrastructure.redis.FraudRedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@Slf4j
public class FraudDetectionService {
    private final FraudRedisService redisService;
    private final FraudEventProducer eventProducer;

    public FraudDetectionService(FraudRedisService redisService, FraudEventProducer eventProducer) {
        this.redisService = redisService;
        this.eventProducer = eventProducer;
    }

    @Value("${fraud.max-amount:10000}")
    private BigDecimal maxAmount;

    @Value("${fraud.max-transactions:3}")
    private int maxTransactions;

    @Value("${fraud.time-window-seconds:60}")
    private int timeWindowSeconds;


    public void analyze(PaymentInitiatedEvent event){
        log.info("🔍 Fraud analizi başlıyor | paymentId: {} | hesap: {} | miktar: {}",
                event.paymentId(), event.sourceAccountId(), event.amount());

        if (redisService.isFlagged(event.sourceAccountId())){
            sendFraudDetected(event,"Hesap daha önce fraud olarak işaretlendi");
            return;
        }

        if (event.amount().compareTo(maxAmount) > 0) {
            sendFraudDetected(event,
                    "Yüksek tutar tespit edildi: " + event.amount() + " TRY");
            return;
        }

        long txCount = redisService.incrementAndGetTransactionCount(event.sourceAccountId(), timeWindowSeconds);
        if (txCount >= maxTransactions) {
            redisService.markAsFraud(event.sourceAccountId());
            sendFraudDetected(event,
                    timeWindowSeconds + " saniyede " + txCount + " işlem tespit edildi");
            return;
        }

        log.info("✅ Fraud analizi geçildi | paymentId: {}", event.paymentId());

        eventProducer.sendFraudApproved(new FraudEvent.FraudApproved(
                event.paymentId(),
                event.sourceAccountId(),
                LocalDateTime.now()
        ));

    }

    private void sendFraudDetected(PaymentInitiatedEvent event, String reason) {
        log.warn("🚨 FRAUD TESPİT EDİLDİ | paymentId: {} | sebep: {}",
                event.paymentId(), reason);

        redisService.markAsFraud(event.sourceAccountId());
        eventProducer.sendFraudDetected(new FraudEvent.FraudDetected(
                event.paymentId(),
                event.sourceAccountId(),
                reason,
                event.amount(),
                LocalDateTime.now()
        ));
    }


}
