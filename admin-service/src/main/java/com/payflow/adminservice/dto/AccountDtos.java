package com.payflow.adminservice.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class AccountDtos {

    public record AccountResponse(
            String id,
            String ownerId,
            String status,
            String statusReason,
            BigDecimal balance,
            String currency,
            String accountNumber,
            LocalDateTime createdAt
    ) {}

    public record BalanceResponse(
            String accountId,
            BigDecimal balance,
            String currency,
            LocalDateTime checkedAt
    ) {}

    public record TransactionResponse(
            String id,
            String accountId,
            String paymentId,
            String type,
            BigDecimal amount,
            String currency,
            BigDecimal balanceAfter,
            String description,
            LocalDateTime createdAt
    ) {}

    public record TransactionPageResponse(
            List<TransactionResponse> transactions,
            int currentPage,
            int totalPages,
            long totalElements
    ) {}
}