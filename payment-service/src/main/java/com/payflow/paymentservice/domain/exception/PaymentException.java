package com.payflow.paymentservice.domain.exception;

public abstract sealed class PaymentException extends RuntimeException
        permits PaymentException.NotFound,
        PaymentException.InvalidStatus,
        PaymentException.InsufficientFunds {

    protected PaymentException(String message) {
        super(message);
    }

    public static final class NotFound extends PaymentException {
        public NotFound(String paymentId) {
            super("Ödeme bulunamadı: " + paymentId);
        }
    }

    public static final class InvalidStatus extends PaymentException {
        public InvalidStatus(String paymentId, String status) {
            super("Geçersiz durum değişikliği. Ödeme: " + paymentId + ", Durum: " + status);
        }
    }

    public static final class InsufficientFunds extends PaymentException {
        public InsufficientFunds(String accountId) {
            super("Yetersiz bakiye. Hesap: " + accountId);
        }
    }
}