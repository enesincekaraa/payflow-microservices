package com.payflow.paymentservice.infrastructure.kafka;

public final class KafkaTopics {
    private KafkaTopics() {}

    // payment-service üretir, account-service tüketir
    public static final String PAYMENT_INITIATED = "payment-initiated";

    // account-service üretir, payment-service tüketir
    public static final String ACCOUNT_DEBITED = "account-debited";

    // payment-service üretir, notification-service tüketir
    public static final String PAYMENT_COMPLETED = "payment-completed";
    public static final String PAYMENT_FAILED    = "payment-failed";
}
