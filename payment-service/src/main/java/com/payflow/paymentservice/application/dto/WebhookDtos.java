package com.payflow.paymentservice.application.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class WebhookDtos {
    public record WebhookPayload(
            String paymentId,
            String event,
            String status,
            BigDecimal amount,
            String currency,
            String sourceAccountId,
            LocalDateTime timestamp
    ){}
    public record RegisterWebhookRequest(
            String clientId,
            String url,
            String events
    ) {}

    public record WebhookResponse(
            String id,
            String clientId,
            String url,
            String events,
            boolean active,
            LocalDateTime createdAt
    ) {}

    public record DeliveryResponse(
            String id,
            String paymentId,
            String event,
            String url,
            int statusCode,
            int attemptCount,
            boolean success,
            String errorMessage,
            LocalDateTime createdAt,
            LocalDateTime deliveredAt
    ) {}


}
