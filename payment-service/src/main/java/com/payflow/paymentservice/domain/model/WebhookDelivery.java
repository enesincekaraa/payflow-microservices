package com.payflow.paymentservice.domain.model;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "webhook_deliveries")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WebhookDelivery {
    @Id
    private String id;

    @Column(nullable = false)
    private String webhookId;

    @Column(nullable = false)
    private String paymentId;

    @Column(nullable = false)
    private String event;

    @Column(nullable = false)
    private String url;

    private int statusCode;
    private int attemptCount;
    private boolean success;
    private String errorMessage;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime deliveredAt;

    public static WebhookDelivery create(
            String webhookId, String paymentId,
            String event, String url) {

        WebhookDelivery delivery = new WebhookDelivery();
        delivery.id           = UUID.randomUUID().toString();
        delivery.webhookId    = webhookId;
        delivery.paymentId    = paymentId;
        delivery.event        = event;
        delivery.url          = url;
        delivery.attemptCount = 0;
        delivery.success      = false;
        delivery.createdAt    = LocalDateTime.now();
        return delivery;
    }

    public void recordSuccess(int statusCode) {
        this.statusCode   = statusCode;
        this.success      = true;
        this.attemptCount++;
        this.deliveredAt  = LocalDateTime.now();
    }
    public void recordFailure(String errorMessage) {
        this.errorMessage = errorMessage;
        this.success      = false;
        this.attemptCount++;
    }
}
