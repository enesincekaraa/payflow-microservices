package com.payflow.accountservice.domain.event;

import com.payflow.accountservice.domain.model.Money;
import java.time.LocalDateTime;


public sealed interface AccountEvent
        permits AccountEvent.AccountCreated,
        AccountEvent.MoneyDeposited,
        AccountEvent.MoneyWithdrawn,
        AccountEvent.AccountSuspended,
        AccountEvent.AccountClosed {

    record AccountCreated(
            String accountId,
            String ownerId,
            Money initialBalance,
            LocalDateTime createdAt
    ) implements AccountEvent {}

    record MoneyDeposited(
            String accountId,
            Money amount,
            Money balanceAfter,
            String description,
            LocalDateTime at
    ) implements AccountEvent {}

    record MoneyWithdrawn(
            String accountId,
            Money amount,
            Money balanceAfter,
            String description,
            LocalDateTime at
    ) implements AccountEvent {}

    record AccountSuspended(
            String accountId,
            String reason,
            LocalDateTime at
    ) implements AccountEvent {}

    record AccountClosed(
            String accountId,
            String reason,
            LocalDateTime at
    ) implements AccountEvent {}
}