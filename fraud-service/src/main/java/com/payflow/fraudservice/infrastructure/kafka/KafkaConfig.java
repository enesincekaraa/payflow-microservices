package com.payflow.fraudservice.infrastructure.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

    @Bean
    public NewTopic fraudDetectedTopic() {
        return TopicBuilder.name(KafkaTopics.FRAUD_DETECTED)
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic fraudApprovedTopic() {
        return TopicBuilder.name(KafkaTopics.FRAUD_APPROVED)
                .partitions(3)
                .replicas(1)
                .build();
    }
}