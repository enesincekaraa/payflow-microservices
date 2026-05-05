package com.payflow.adminservice.service;

import com.payflow.adminservice.client.AccountClient;
import com.payflow.adminservice.client.PaymentClient;
import com.payflow.adminservice.dto.AccountDtos;
import com.payflow.adminservice.dto.AdminDtos;
import com.payflow.adminservice.dto.PaymentDtos;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@Slf4j
public class AdminService {
    private final AccountClient accountClient;
    private final PaymentClient paymentClient;

    public AdminService(AccountClient accountClient, PaymentClient paymentClient) {
        this.accountClient = accountClient;
        this.paymentClient = paymentClient;
    }

    // ─── Hesap İşlemleri ──────────────────────────────────

    public AccountDtos.AccountResponse getAccount(String accountId) {
        log.info("Admin hesap sorguluyor: {}", accountId);
        return accountClient.getAccount(accountId);
    }

    public AccountDtos.AccountResponse suspendAccount(
            String accountId, String reason) {
        log.warn("Admin hesap askıya alıyor: {} | sebep: {}",
                accountId, reason);
        return accountClient.suspendAccount(accountId, reason);
    }

    public AccountDtos.AccountResponse reactivateAccount(String accountId) {
        log.info("Admin hesap reaktive ediyor: {}", accountId);
        return accountClient.reactivateAccount(accountId);
    }

    public AccountDtos.TransactionPageResponse getTransactions(
            String accountId, int page, int size) {
        return accountClient.getTransactions(accountId, page, size);
    }

    // ─── Ödeme İşlemleri ──────────────────────────────────

    public PaymentDtos.PaymentResponse getPayment(String paymentId) {
        return paymentClient.getPayment(paymentId);
    }

    public List<PaymentDtos.PaymentResponse> getPaymentsByAccount(
            String accountId) {
        return paymentClient.getPaymentsByAccount(accountId);
    }


    // ─── Raporlar ─────────────────────────────────────────

    public AdminDtos.FraudReport getFraudReport(
            List<String> suspendedAccountIds) {

        List<AccountDtos.AccountResponse> suspendedAccounts = suspendedAccountIds
                .stream()
                .map(accountClient::getAccount)
                .filter(a -> "SUSPENDED".equals(a.status()))
                .toList();

        return new AdminDtos.FraudReport(
                suspendedAccounts.size(),
                suspendedAccounts
        );
    }


    // ─── Dashboard ────────────────────────────────────────

    public AdminDtos.DashboardStats getDashboard(List<String> accountIds,List<String> paymentIds){

        List<AccountDtos.AccountResponse> accounts=accountIds.stream()
                .map(accountClient::getAccount)
                .toList();

        List<PaymentDtos.PaymentResponse> payments = paymentIds.stream()
                .map(paymentClient::getPayment)
                .toList();


        long activeAccounts = accounts.stream()
                .filter(a->"ACTIVE".equals(a.status())).count();

        long suspendedAccounts = accounts.stream()
                .filter(a->"SUSPENDED".equals(a.status())).count();


        long closedAccounts = accounts.stream()
                .filter(a -> "CLOSED".equals(a.status())).count();

        long completedPayments = payments.stream()
                .filter(p -> "COMPLETED".equals(p.status())).count();

        long failedPayments = payments.stream()
                .filter(p -> "FAILED".equals(p.status())).count();

        long pendingPayments = payments.stream()
                .filter(p -> "PENDING".equals(p.status())).count();

        BigDecimal totalVolume = payments.stream()
                .filter(p->"COMPLETED".equals(p.status()))
                .map(PaymentDtos.PaymentResponse::amount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new AdminDtos.DashboardStats(
                accounts.size(),
                activeAccounts,
                suspendedAccounts,
                closedAccounts,
                payments.size(),
                completedPayments,
                failedPayments,
                pendingPayments,
                totalVolume
        );

    }
}
