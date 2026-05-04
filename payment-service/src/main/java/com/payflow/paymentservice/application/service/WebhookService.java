package com.payflow.paymentservice.application.service;

import com.payflow.paymentservice.application.dto.WebhookDtos;
import com.payflow.paymentservice.domain.model.WebHookRegistration;
import com.payflow.paymentservice.domain.model.WebhookDelivery;
import com.payflow.paymentservice.infrastructure.repository.WebhookDeliveryRepository;
import com.payflow.paymentservice.infrastructure.repository.WebhookRegistrationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@Slf4j
public class WebhookService {
    private final WebhookRegistrationRepository webhookRepository;
    private final WebhookDeliveryRepository deliveryRepository;

    private final RestTemplate restTemplate;

    public WebhookService(WebhookRegistrationRepository webhookRepository, WebhookDeliveryRepository deliveryRepository, RestTemplate restTemplate) {
        this.webhookRepository = webhookRepository;
        this.deliveryRepository = deliveryRepository;
        this.restTemplate = restTemplate;
    }

    private static final int MAX_RETRY = 3;


    @Transactional
    public WebhookDtos.WebhookResponse register(WebhookDtos.RegisterWebhookRequest req){

        WebHookRegistration webhook = WebHookRegistration.create(
                req.clientId(),
                req.url(),
                req.events()
        );
        WebHookRegistration saved = webhookRepository.save(webhook);
        log.info("Webhook kaydedildi | clientId: {} | url: {}",
                saved.getClientId(), saved.getUrl());
        return toResponse(saved);
    }

    @Transactional
    public void dispatch(WebhookDtos.WebhookPayload payload){
        List<WebHookRegistration> webhooks =
                webhookRepository.findByActiveTrue();
        if (webhooks.isEmpty()){
            log.debug("Kayıtlı webhook yok.");
            return;
        }
        webhooks.stream().filter(w-> w.listensTo(payload.event()))
                .forEach(webhook -> send(webhook,payload));
    }

    private void send(WebHookRegistration webhook, WebhookDtos.WebhookPayload payload) {
        WebhookDelivery delivery = WebhookDelivery.create(
                webhook.getId(),
                payload.paymentId(),
                payload.event(),
                webhook.getUrl()
        );

        for (int attempt =1; attempt <= MAX_RETRY; attempt++) {
            try {
                log.info("Webhook gönderiliyor | url: {} | attempt: {}/{}",
                        webhook.getUrl(), attempt, MAX_RETRY);

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                headers.set("X-Payflow-Event", payload.event());
                headers.set("X-PayFlow-PaymentId", payload.paymentId());

                HttpEntity<WebhookDtos.WebhookPayload> req = new HttpEntity<>(payload, headers);

                var response = restTemplate.postForEntity(
                        webhook.getUrl(),req, String.class
                );

                delivery.recordSuccess(
                        response.getStatusCode().value()
                );

                log.info("✅ Webhook başarılı | url: {} | status: {}",
                        webhook.getUrl(),
                        response.getStatusCode().value());

                break;
            }catch (Exception e){

            }
        }
    }


    @Transactional(readOnly = true)
    public List<WebhookDtos.WebhookResponse> getWebhooks(String clientId){
        return webhookRepository.findByClientId(clientId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<WebhookDtos.DeliveryResponse> getDeliveries(String paymentId) {
        return deliveryRepository.findByPaymentId(paymentId)
                .stream()
                .map(this::toDeliveryResponse)
                .toList();
    }



    private WebhookDtos.WebhookResponse toResponse(WebHookRegistration w) {
        return new WebhookDtos.WebhookResponse(
                w.getId(), w.getClientId(), w.getUrl(),
                w.getEvents(), w.isActive(), w.getCreatedAt()
        );
    }

    private WebhookDtos.DeliveryResponse toDeliveryResponse(WebhookDelivery d) {
        return new WebhookDtos.DeliveryResponse(
                d.getId(), d.getPaymentId(), d.getEvent(),
                d.getUrl(), d.getStatusCode(), d.getAttemptCount(),
                d.isSuccess(), d.getErrorMessage(),
                d.getCreatedAt(), d.getDeliveredAt()
        );
    }
}
