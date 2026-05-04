package com.payflow.paymentservice.infrastructure.repository;

import com.payflow.paymentservice.domain.model.WebHookRegistration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WebhookRegistrationRepository extends JpaRepository<WebHookRegistration,String> {
    List<WebHookRegistration> findByActiveTrue();

    List<WebHookRegistration> findByClientId(String clientId);
}
