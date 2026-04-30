package com.payflow.paymentservice.infrastructure.repository;

import com.payflow.paymentservice.domain.model.Payment;
import com.payflow.paymentservice.domain.model.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, String> {
    List<Payment> findBySourceAccountId(String accountId);
    List<Payment> findByStatus(PaymentStatus status);

    List<Payment> findByStatusAndCreatedAtBefore(PaymentStatus paymentStatus, LocalDateTime cutoff);
}
