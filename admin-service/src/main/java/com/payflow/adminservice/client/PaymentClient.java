package com.payflow.adminservice.client;

import com.payflow.adminservice.config.FeignConfig;
import com.payflow.adminservice.dto.PaymentDtos.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@FeignClient(name = "payment-client", url = "${services.payment-url}",configuration = FeignConfig.class)
public interface PaymentClient {

    @GetMapping("/api/payments/{id}")
    PaymentResponse getPayment(@PathVariable String id);

    @GetMapping("/api/payments/account/{accountId}")
    List<PaymentResponse> getPaymentsByAccount(
            @PathVariable String accountId);
}