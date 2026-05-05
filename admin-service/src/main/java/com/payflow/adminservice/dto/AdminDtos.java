package com.payflow.adminservice.dto;

import java.math.BigDecimal;
import java.util.List;

public class AdminDtos {

    public record DashboardStats(
            long totalAccounts,
            long activeAccounts,
            long suspendedAccounts,
            long closedAccounts,
            long totalPayments,
            long completedPayments,
            long failedPayments,
            long pendingPayments,
            BigDecimal totalVolume
    ) {}

    public record FraudReport(
            long suspendedAccounts,
            List<AccountDtos.AccountResponse> suspendedAccountList
    ) {}

    public record SuspendAccountRequest(
            String reason
    ) {}
    public record DashboardRequest(
            List<String> accountIds,
            List<String> paymentIds
    ) {}

    public record FraudReportRequest(
            List<String> accountIds
    ) {}
}