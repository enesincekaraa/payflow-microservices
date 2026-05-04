package com.payflow.paymentservice.presentation.controller;

import com.payflow.paymentservice.application.dto.WebhookDtos.*;
import com.payflow.paymentservice.application.service.WebhookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/webhooks")
@RequiredArgsConstructor
public class WebhookController {

    private final WebhookService webhookService;

    @PostMapping
    public ResponseEntity<WebhookResponse> register(
            @RequestBody RegisterWebhookRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(webhookService.register(request));
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<WebhookResponse>> getWebhooks(
            @PathVariable String clientId) {
        return ResponseEntity.ok(webhookService.getWebhooks(clientId));
    }

    @GetMapping("/deliveries/{paymentId}")
    public ResponseEntity<List<DeliveryResponse>> getDeliveries(
            @PathVariable String paymentId) {
        return ResponseEntity.ok(webhookService.getDeliveries(paymentId));
    }
}