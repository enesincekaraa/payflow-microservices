package com.payflow.adminservice.client;


import com.payflow.adminservice.config.FeignConfig;
import com.payflow.adminservice.dto.AccountDtos;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "account-client",url = "${services.account-url}",configuration = FeignConfig.class)
public interface AccountClient {

    // Tüm hesapları getir
    @GetMapping("/api/accounts/owner/{ownerId}")
    List<AccountDtos.AccountResponse> getAccountsByOwner(@PathVariable String ownerId);

    // Belirli hesabı getir
    @GetMapping("/api/accounts/{id}")
    AccountDtos.AccountResponse getAccount(@PathVariable String id);

    // Bakiye sorgula
    @GetMapping("/api/accounts/{id}/balance")
    AccountDtos.BalanceResponse getBalance(@PathVariable String id);

    // Hesabı askıya al
    @PatchMapping("/api/accounts/{id}/suspend")
    AccountDtos.AccountResponse suspendAccount(
            @PathVariable String id,
            @RequestParam String reason);

    // Hesabı reaktive et
    @PatchMapping("/api/accounts/{id}/reactivate")
    AccountDtos.AccountResponse reactivateAccount(@PathVariable String id);

    // İşlem geçmişi
    @GetMapping("/api/accounts/{id}/transactions")
    AccountDtos.TransactionPageResponse getTransactions(
            @PathVariable String id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size);
}
