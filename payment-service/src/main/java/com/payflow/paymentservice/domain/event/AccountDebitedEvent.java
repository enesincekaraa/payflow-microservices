package com.payflow.paymentservice.domain.event;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record AccountDebitedEvent(
        String paymentId,
        String accountId,
        BigDecimal amount,
        String currency,
        boolean success,
        String failureReason,
        LocalDateTime at
) {}