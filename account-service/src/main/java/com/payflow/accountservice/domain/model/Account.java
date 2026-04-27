package com.payflow.accountservice.domain.model;

import com.payflow.accountservice.domain.event.AccountEvent;
import com.payflow.accountservice.domain.exception.AccountException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;


@Entity
@Table(name = "accounts")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Account {
    @Id
    private String id;

    @Column(nullable = false)
    private String ownerId;

    @Column(nullable = false)
    private String accountNumber;

    @Embedded
    private Money balance;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountStatus status;

    private String statusReason;
    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @Transient
    public final List<AccountEvent> domainEvents=new ArrayList<>();

    public static Account create(String ownerId,Money initialDeposit){
        if (ownerId == null || ownerId.isBlank())
            throw new IllegalArgumentException("ownerId boş olamaz");

        Account account = new Account();
        account.id = UUID.randomUUID().toString();
        account.ownerId = ownerId;
        account.accountNumber = "TR" +System.currentTimeMillis();
        account.balance = initialDeposit;
        account.status = AccountStatus.ACTIVE;
        account.createdAt = LocalDateTime.now();

        account.registerEvent(new AccountEvent.AccountCreated(
                account.id,
                account.ownerId,
                account.balance,
                account.createdAt
        ));

        return account;

    }

    public void deposit(Money amount,String description){
        ensureActive();
        this.balance = this.balance.add(amount);
        this.updatedAt = LocalDateTime.now();
        registerEvent(new AccountEvent.MoneyDeposited(
                this.id,
                amount,
                this.balance,
                description,
               LocalDateTime.now()
        ));
    }

    public void withdraw(Money amount,String description){
        ensureActive();
        if (this.balance.amount().compareTo(amount.amount()) < 0)
            throw new AccountException.InsufficientFunds(this.balance, amount);
        this.balance = this.balance.subtract(amount);
        this.updatedAt = LocalDateTime.now();
        registerEvent(new AccountEvent.MoneyWithdrawn(
                this.id,amount,this.balance,description,LocalDateTime.now()
        ));
    }


    public void suspend(String reason){
      if (this.status ==AccountStatus.CLOSED)
          throw new AccountException.AccountInactive(this.id);
      if (this.status == AccountStatus.SUSPENDED)
          throw new IllegalStateException("Hesap zaten askıda");
      this.status = AccountStatus.SUSPENDED;
      this.statusReason = reason;
      this.updatedAt = LocalDateTime.now();
      registerEvent(new AccountEvent.AccountSuspended(
              this.id,reason,LocalDateTime.now()));

    }
    public void reactivate() {
        if (this.status == AccountStatus.CLOSED)
            throw new AccountException.AccountInactive(this.id);
        if (this.status == AccountStatus.ACTIVE)
            throw new IllegalStateException("Hesap zaten aktif");
        this.status       = AccountStatus.ACTIVE;
        this.statusReason = null;
        this.updatedAt    = LocalDateTime.now();
    }

    public void close(String reason) {
        if (this.status == AccountStatus.CLOSED) return;
        if (!this.balance.isZero())
            throw new IllegalStateException(
                    "Bakiyesi olan hesap kapatılamaz. Bakiye: " + this.balance
            );
        this.status       = AccountStatus.CLOSED;
        this.statusReason = reason;
        this.updatedAt    = LocalDateTime.now();
        registerEvent(new AccountEvent.AccountClosed(
                this.id, reason, LocalDateTime.now()
        ));
    }


    private void ensureActive() {
        if (this.status != AccountStatus.ACTIVE)
            throw new AccountException.AccountInactive(this.id);
    }


    private void registerEvent(AccountEvent event){
        this.domainEvents.add(event);
    }

    public List<AccountEvent> getDomainEvents() {
        return Collections.unmodifiableList(domainEvents);
    }

    public void clearDomainEvents() {
        this.domainEvents.clear();
    }

    public boolean isActive()    { return this.status == AccountStatus.ACTIVE; }
    public boolean isSuspended() { return this.status == AccountStatus.SUSPENDED; }
    public boolean isClosed()    { return this.status == AccountStatus.CLOSED; }

}