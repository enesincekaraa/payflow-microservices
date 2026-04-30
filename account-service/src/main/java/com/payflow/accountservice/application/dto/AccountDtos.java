package com.payflow.accountservice.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class AccountDtos {

    public record CreateAccountRequest(
            @NotBlank(message = "Sahip ID boş olamaz")
            String ownerId,

            @NotBlank(message = "Para birimi boş olamaz")
            String currency,

            @NotNull @Positive
            BigDecimal initialDeposit
    ) {}

    public record DepositRequest(
            @NotNull @Positive BigDecimal amount,
            @NotBlank String currency,
            String description
    ) {}

    public record WithdrawRequest(
            @NotNull @Positive BigDecimal amount,
            @NotBlank String currency,
            String description
    ) {}

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
    ){}

    public record TransactionPageResponse(
            List<TransactionResponse> transactions,
            int currentPage,
            int totalPages,
            long totalElements
    ) {}
}