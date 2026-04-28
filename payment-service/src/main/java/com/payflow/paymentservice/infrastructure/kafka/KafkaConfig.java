package com.payflow.paymentservice.infrastructure.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {
    @Bean
    public NewTopic paymentInitiatedTopic() {
        return TopicBuilder.name(KafkaTopics.PAYMENT_INITIATED)
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic accountDebitedTopic() {
        return TopicBuilder.name(KafkaTopics.ACCOUNT_DEBITED)
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic paymentCompletedTopic() {
        return TopicBuilder.name(KafkaTopics.PAYMENT_COMPLETED)
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic paymentFailedTopic() {
        return TopicBuilder.name(KafkaTopics.PAYMENT_FAILED)
                .partitions(3)
                .replicas(1)
                .build();
    }
}
