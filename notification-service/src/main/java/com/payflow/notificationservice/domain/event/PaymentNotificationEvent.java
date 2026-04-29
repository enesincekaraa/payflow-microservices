package com.payflow.notificationservice.domain.event;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentNotificationEvent(
        String paymentId,
        String sourceAccountId,
        BigDecimal amount,
        String currency,
        boolean success,
        String failureReason,
        LocalDateTime completedAt
) {}
