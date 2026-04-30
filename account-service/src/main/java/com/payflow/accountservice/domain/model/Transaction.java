package com.payflow.accountservice.domain.model;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "transactions")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Transaction {

    @Id
    private String id;

    @Column(nullable = false)
    private String accountId;

    private String paymentId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType type;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private String currency;

    @Column(nullable = false)
    private BigDecimal balanceAfter;
    private String description;

    @Column(nullable = false)
    private LocalDateTime createdAt;


    public static Transaction deposit(
            String accountId,
            BigDecimal amount,
            String currency,
            BigDecimal balanceAfter,
            String description
    ){
        return create(accountId,null,TransactionType.DEPOSIT,amount,currency,balanceAfter,description);
    }

    public static Transaction withdrawal(
            String accountId,
            BigDecimal amount,
            String currency,
            BigDecimal balanceAfter,
            String description) {

        return create(accountId, null, TransactionType.WITHDRAWAL,
                amount, currency, balanceAfter, description);
    }

    public static Transaction payment(
            String accountId,
            String paymentId,
            BigDecimal amount,
            String currency,
            BigDecimal balanceAfter,
            String description) {

        return create(accountId, paymentId, TransactionType.PAYMENT,
                amount, currency, balanceAfter, description);
    }


    private static Transaction create(
            String accountId,
            String paymentId,
            TransactionType type,
            BigDecimal amount,
            String currency,
            BigDecimal balanceAfter,
            String description) {

        Transaction tx = new Transaction();
        tx.id           = UUID.randomUUID().toString();
        tx.accountId    = accountId;
        tx.paymentId    = paymentId;
        tx.type         = type;
        tx.amount       = amount;
        tx.currency     = currency;
        tx.balanceAfter = balanceAfter;
        tx.description  = description;
        tx.createdAt    = LocalDateTime.now();
        return tx;
    }
}
