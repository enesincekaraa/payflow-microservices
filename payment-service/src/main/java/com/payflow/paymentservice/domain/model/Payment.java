package com.payflow.paymentservice.domain.model;

import com.payflow.paymentservice.domain.exception.PaymentException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "payments")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payment {
    @Id
    private String id;

    @Column(nullable = false)
    private String sourceAccountId;

    @Column(nullable = false)
    private String targetAccountId;


    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private String currency;

    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status;

    private String failureReason;


    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;


    public static Payment create(
            String sourceAccountId,
            String targetAccountId,
            BigDecimal amount,
            String currency,
            String description
    ){
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Ödeme miktarı pozitif olmalı");

        }
        if (sourceAccountId.equals(targetAccountId)) {
            throw new IllegalArgumentException("Kaynak ve hedef hesap aynı olamaz");

        }
        Payment payment = new Payment();
        payment.id = UUID.randomUUID().toString();
        payment.sourceAccountId = sourceAccountId;
        payment.targetAccountId = targetAccountId;
        payment.amount = amount;
        payment.currency = currency;
        payment.description = description;
        payment.status = PaymentStatus.PENDING;
        payment.createdAt = LocalDateTime.now();
        return payment;
    }

    public void complete() {
        if (this.status != PaymentStatus.PENDING)
            throw new PaymentException.InvalidStatus(this.id, this.status.name());

        this.status    = PaymentStatus.COMPLETED;
        this.updatedAt = LocalDateTime.now();
    }
    public void fail(String reason) {
        if (this.status != PaymentStatus.PENDING)
            throw new PaymentException.InvalidStatus(this.id, this.status.name());

        this.status        = PaymentStatus.FAILED;
        this.failureReason = reason;
        this.updatedAt     = LocalDateTime.now();
    }

    public void cancel() {
        if (this.status != PaymentStatus.PENDING)
            throw new PaymentException.InvalidStatus(this.id, this.status.name());

        this.status    = PaymentStatus.CANCELLED;
        this.updatedAt = LocalDateTime.now();
    }

    public boolean isPending()   { return this.status == PaymentStatus.PENDING; }
    public boolean isCompleted() { return this.status == PaymentStatus.COMPLETED; }
    public boolean isFailed()    { return this.status == PaymentStatus.FAILED; }





}
