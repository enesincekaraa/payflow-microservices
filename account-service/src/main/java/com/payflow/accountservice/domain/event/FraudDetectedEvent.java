package com.payflow.accountservice.domain.event;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record FraudDetectedEvent(
        String paymentId,
        String accountId,
        String reason,
        BigDecimal amount,
        LocalDateTime detectedAt
) {
}
