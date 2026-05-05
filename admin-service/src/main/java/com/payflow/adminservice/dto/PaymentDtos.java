package com.payflow.adminservice.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PaymentDtos {

    public record PaymentResponse(
            String id,
            String sourceAccountId,
            String targetAccountId,
            BigDecimal amount,
            String currency,
            String status,
            String failureReason,
            String description,
            LocalDateTime createdAt
    ) {}
}