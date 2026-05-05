package com.payflow.adminservice.controller;

import com.payflow.adminservice.dto.AccountDtos;
import com.payflow.adminservice.dto.AdminDtos;
import com.payflow.adminservice.dto.PaymentDtos;
import com.payflow.adminservice.service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }
    // ─── Hesap İşlemleri ──────────────────────────────────

    @GetMapping("/accounts/{id}")
    public ResponseEntity<AccountDtos.AccountResponse> getAccount(
            @PathVariable String id) {
        return ResponseEntity.ok(adminService.getAccount(id));
    }

    @PatchMapping("/accounts/{id}/suspend")
    public ResponseEntity<AccountDtos.AccountResponse> suspendAccount(
            @PathVariable String id,
            @RequestBody AdminDtos.SuspendAccountRequest request) {
        return ResponseEntity.ok(
                adminService.suspendAccount(id, request.reason()));
    }

    @PatchMapping("/accounts/{id}/reactivate")
    public ResponseEntity<AccountDtos.AccountResponse> reactivateAccount(
            @PathVariable String id) {
        return ResponseEntity.ok(
                adminService.reactivateAccount(id));
    }

    @GetMapping("/accounts/{id}/transactions")
    public ResponseEntity<AccountDtos.TransactionPageResponse> getTransactions(
            @PathVariable String id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(
                adminService.getTransactions(id, page, size));
    }

    // ─── Ödeme İşlemleri ──────────────────────────────────

    @GetMapping("/payments/{id}")
    public ResponseEntity<PaymentDtos.PaymentResponse> getPayment(
            @PathVariable String id) {
        return ResponseEntity.ok(adminService.getPayment(id));
    }

    @GetMapping("/payments/account/{accountId}")
    public ResponseEntity<List<PaymentDtos.PaymentResponse>> getPaymentsByAccount(
            @PathVariable String accountId) {
        return ResponseEntity.ok(
                adminService.getPaymentsByAccount(accountId));
    }

    // ─── Dashboard ────────────────────────────────────────

    @PostMapping("/dashboard")
    public ResponseEntity<AdminDtos.DashboardStats> getDashboard(
            @RequestBody AdminDtos.DashboardRequest request) {
        return ResponseEntity.ok(
                adminService.getDashboard(
                        request.accountIds(),
                        request.paymentIds()));
    }

    // ─── Raporlar ─────────────────────────────────────────

    @PostMapping("/reports/fraud")
    public ResponseEntity<AdminDtos.FraudReport> getFraudReport(
            @RequestBody AdminDtos.FraudReportRequest request) {
        return ResponseEntity.ok(
                adminService.getFraudReport(request.accountIds()));
    }

}
