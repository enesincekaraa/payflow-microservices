package com.payflow.fraudservice.domain.event;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public sealed interface FraudEvent  permits
        FraudEvent.FraudDetected,FraudEvent.FraudApproved{

    record FraudDetected(
            String paymentId,
            String accountId,
            String reason,
            BigDecimal amount,
            LocalDateTime detectedAt
    ) implements FraudEvent {
    }

    record FraudApproved(
            String paymentId,
            String accountId,
            LocalDateTime approvedAt
    ) implements FraudEvent {}
}
