package com.payflow.paymentservice.domain.event;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public sealed interface PaymentEvent permits
        PaymentEvent.PaymentInitiated,
        PaymentEvent.PaymentCompleted,
        PaymentEvent.PaymentFailed{
    record PaymentInitiated(
            String paymentId,
            String sourceAccountId,
            String targetAccountId,
            BigDecimal amount,
            String currency,
            String description,
            LocalDateTime initiatedAt
    ) implements PaymentEvent {}
    record PaymentCompleted(
            String paymentId,
            String sourceAccountId,
            BigDecimal amount,
            String currency,
            LocalDateTime completedAt
    ) implements PaymentEvent {}

    record PaymentFailed(
            String paymentId,
            String reason,
            LocalDateTime failedAt
    ) implements PaymentEvent {}
}
