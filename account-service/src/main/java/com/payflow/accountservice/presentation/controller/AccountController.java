package com.payflow.accountservice.presentation.controller;

import com.payflow.accountservice.application.dto.AccountDtos;
import com.payflow.accountservice.application.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    public ResponseEntity<AccountDtos.AccountResponse> createAccount(
            @Valid @RequestBody AccountDtos.CreateAccountRequest req
    ){
        return  ResponseEntity.status(HttpStatus.CREATED)
                .body(accountService.createAccount(req));
    }
    @GetMapping("/{id}")
    public ResponseEntity<AccountDtos.AccountResponse> getAccount(@PathVariable String id) {
        return ResponseEntity.ok(accountService.getAccount(id));
    }

    @GetMapping("/{id}/balance")
    public ResponseEntity<AccountDtos.BalanceResponse> getBalance(@PathVariable String id) {
        return ResponseEntity.ok(accountService.getBalance(id));
    }

    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<List<AccountDtos.AccountResponse>> getByOwner(
            @PathVariable String ownerId) {
        return ResponseEntity.ok(accountService.getAccountsByOwner(ownerId));
    }

    @PostMapping("/{id}/deposit")
    public ResponseEntity<AccountDtos.AccountResponse> deposit(
            @PathVariable String id,
            @Valid @RequestBody AccountDtos.DepositRequest request) {
        return ResponseEntity.ok(accountService.deposit(id, request));
    }

    @PostMapping("/{id}/withdraw")
    public ResponseEntity<AccountDtos.AccountResponse> withdraw(
            @PathVariable String id,
            @Valid @RequestBody AccountDtos.WithdrawRequest request) {
        return ResponseEntity.ok(accountService.withdraw(id, request));
    }

    @PatchMapping("/{id}/suspend")
    public ResponseEntity<AccountDtos.AccountResponse> suspend(
            @PathVariable String id,
            @RequestParam String reason) {
        return ResponseEntity.ok(accountService.suspendAccount(id, reason));
    }

    @PatchMapping("/{id}/reactivate")
    public ResponseEntity<AccountDtos.AccountResponse> reactivate(@PathVariable String id) {
        return ResponseEntity.ok(accountService.reactivateAccount(id));
    }

    @PatchMapping("/{id}/close")
    public ResponseEntity<AccountDtos.AccountResponse> close(
            @PathVariable String id,
            @RequestParam String reason) {
        return ResponseEntity.ok(accountService.closeAccount(id, reason));
    }

    @GetMapping("/{id}/transactions")
    public ResponseEntity<AccountDtos.TransactionPageResponse> getTransactions(
            @PathVariable String id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        return ResponseEntity.ok(accountService.getTransactions(id, page, size));
    }

    @GetMapping("/{id}/transactions/filter")
    public ResponseEntity<List<AccountDtos.TransactionResponse>> getTransactionsByType(
            @PathVariable String id,
            @RequestParam String type) {
        return ResponseEntity.ok(
                accountService.getTransactionsByType(id, type));
    }
}
