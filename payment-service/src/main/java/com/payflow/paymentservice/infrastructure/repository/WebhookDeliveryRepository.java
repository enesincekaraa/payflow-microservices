package com.payflow.paymentservice.infrastructure.repository;

import com.payflow.paymentservice.domain.model.WebhookDelivery;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WebhookDeliveryRepository extends JpaRepository<WebhookDelivery, String> {

    List<WebhookDelivery> findByPaymentId(String paymentId);
    List<WebhookDelivery> findBySuccessFalse();
}
