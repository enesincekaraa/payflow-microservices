package com.payflow.paymentservice.presentation.controller;

import com.payflow.paymentservice.application.dto.PaymentDtos.*;
import com.payflow.paymentservice.application.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<PaymentResponse> initiatePayment(
            @Valid @RequestBody InitiatePaymentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(paymentService.initiatePayment(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponse> getPayment(@PathVariable String id) {
        return ResponseEntity.ok(paymentService.getPayment(id));
    }

    @GetMapping("/account/{accountId}")
    public ResponseEntity<List<PaymentResponse>> getPaymentsByAccount(
            @PathVariable String accountId) {
        return ResponseEntity.ok(paymentService.getPaymentsByAccount(accountId));
    }
}