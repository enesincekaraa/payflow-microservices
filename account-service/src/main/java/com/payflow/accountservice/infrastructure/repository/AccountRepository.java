package com.payflow.accountservice.infrastructure.repository;

import com.payflow.accountservice.domain.model.Account;
import com.payflow.accountservice.domain.model.AccountStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, String> {
    Optional<Account> findByOwnerId(String ownerId);
    List<Account> findAllByOwnerId(String ownerId);
    boolean existsByOwnerId(String ownerId);
    Optional<Account> findByAccountNumber(String accountNumber);
    List<Account> findAllByStatus(AccountStatus status);
}
