package com.payflow.accountservice.domain.event;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentInitiatedEvent (
    String paymentId,
    String sourceAccountId,
    String targetAccountId,
    BigDecimal amount,
    String currency,
    String description,
    LocalDateTime initiatedAt
){}
