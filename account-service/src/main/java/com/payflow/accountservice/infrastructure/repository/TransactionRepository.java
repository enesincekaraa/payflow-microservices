package com.payflow.accountservice.infrastructure.repository;

import com.payflow.accountservice.domain.model.Transaction;
import com.payflow.accountservice.domain.model.TransactionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, String> {

    Page<Transaction> findByAccountIdOrderByCreatedAtDesc(String accountId, Pageable pageable);

    List<Transaction> findByAccountIdAndTypeOrderByCreatedAtDesc(String accountId, TransactionType type);

    List<Transaction> findByCreatedAtBetweenOrderByCreatedAtDesc(
            LocalDateTime from, LocalDateTime to);

    List<Transaction> findByPaymentId(String paymentId);



}
