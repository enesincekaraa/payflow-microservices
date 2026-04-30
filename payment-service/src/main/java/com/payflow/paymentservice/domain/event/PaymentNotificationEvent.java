package com.payflow.paymentservice.domain.event;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentNotificationEvent(
        String paymentId,
        String accountId,
        String reason,
        BigDecimal amount,
        String currency,
        boolean success,
        LocalDateTime detectedAt
) {}