package com.payflow.accountservice.application.service;

import com.payflow.accountservice.application.dto.AccountDtos;
import com.payflow.accountservice.domain.event.AccountEvent;
import com.payflow.accountservice.domain.exception.AccountException;
import com.payflow.accountservice.domain.model.Account;
import com.payflow.accountservice.domain.model.Money;
import com.payflow.accountservice.infrastructure.repository.AccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class AccountService {
    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Transactional
    public AccountDtos.AccountResponse createAccount(AccountDtos.CreateAccountRequest req) {
        if (accountRepository.existsByOwnerId(req.ownerId())) {
            throw new AccountException.DuplicateAccount("Owner already has an account");
        }
        Account account = Account.create(
                req.ownerId(),
                Money.of(req.initialDeposit(), req.currency())
        );
        Account saved = accountRepository.save(account);
        processDomainEvents(saved);
        return toResponse(saved);


    }



    @Transactional
    public AccountDtos.AccountResponse deposit(String accountId, AccountDtos.DepositRequest req){
        Account account = findById(accountId);
        account.deposit(
                Money.of(req.amount(),req.currency()),
                req.description()
        );
        Account saved = accountRepository.save(account);
        processDomainEvents(saved);
        return toResponse(saved);
    }

    @Transactional
    public AccountDtos.AccountResponse withdraw(String accountId, AccountDtos.WithdrawRequest req){
        Account account = findById(accountId);
        account.withdraw(
                Money.of(req.amount(),req.currency()),
                req.description()
        );
        Account saved = accountRepository.save(account);
        processDomainEvents(saved);
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public AccountDtos.AccountResponse getAccount(String accountId){
        Account account = findById(accountId);
        return toResponse(account);
    }

    @Transactional(readOnly = true)
    public AccountDtos.BalanceResponse getBalance(String accountId){
        Account account = findById(accountId);
        return new AccountDtos.BalanceResponse(
                account.getId(),
                account.getBalance().amount(),
                account.getBalance().currency(),
                LocalDateTime.now()
        );
    }

    @Transactional(readOnly = true)
    public List<AccountDtos.AccountResponse> getAccountsByOwner(String ownerId){
        return accountRepository.findAllByOwnerId(ownerId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public AccountDtos.AccountResponse suspendAccount(String accountId,String reason){
        Account account = findById(accountId);
        account.suspend(reason);
        return toResponse(accountRepository.save(account));
    }
    @Transactional
    public AccountDtos.AccountResponse reactivateAccount(String accountId) {
        Account account = findById(accountId);
        account.reactivate();
        return toResponse(accountRepository.save(account));
    }

    @Transactional
    public AccountDtos.AccountResponse closeAccount(String accountId, String reason) {
        Account account = findById(accountId);
        account.close(reason);
        return toResponse(accountRepository.save(account));
    }


    private void processDomainEvents(Account account) {
        account.getDomainEvents().forEach(event -> {
            switch (event) {
                case AccountEvent.AccountCreated e ->
                        log.info("✅ Hesap oluşturuldu | id: {} | bakiye: {}",
                                e.accountId(), e.initialBalance());
                case AccountEvent.MoneyDeposited e ->
                        log.info("💰 Para yatırıldı | id: {} | miktar: {} | bakiye: {}",
                                e.accountId(), e.amount(), e.balanceAfter());
                case AccountEvent.MoneyWithdrawn e ->
                        log.info("💸 Para çekildi | id: {} | miktar: {} | bakiye: {}",
                                e.accountId(), e.amount(), e.balanceAfter());
                case AccountEvent.AccountSuspended e ->
                        log.warn("⚠️ Askıya alındı | id: {} | sebep: {}",
                                e.accountId(), e.reason());
                case AccountEvent.AccountClosed e ->
                        log.warn("🔒 Kapatıldı | id: {} | sebep: {}",
                                e.accountId(), e.reason());
            }
        });
        account.clearDomainEvents();
    }

    private AccountDtos.AccountResponse toResponse(Account account) {
        return new AccountDtos.AccountResponse(
                account.getId(),
                account.getOwnerId(),
                account.getStatus().name(),
                account.getStatusReason(),
                account.getBalance().amount(),
                account.getBalance().currency(),
                account.getAccountNumber(),
                account.getCreatedAt()
        );
    }

    private Account findById(String accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountException.NotFound(accountId));
    }

}
