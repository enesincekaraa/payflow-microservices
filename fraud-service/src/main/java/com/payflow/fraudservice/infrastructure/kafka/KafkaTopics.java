package com.payflow.fraudservice.infrastructure.kafka;

public final class KafkaTopics {

    private KafkaTopics() {}

    public static final String PAYMENT_INITIATED = "payment-initiated";
    public static final String FRAUD_DETECTED    = "fraud-detected";
    public static final String FRAUD_APPROVED    = "fraud-approved";
}