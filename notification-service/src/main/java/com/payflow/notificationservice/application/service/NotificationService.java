package com.payflow.notificationservice.application.service;

import com.payflow.notificationservice.domain.event.PaymentNotificationEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NotificationService {

    public void sendPaymentSuccessNotification(PaymentNotificationEvent event) {
        log.info("║ Zaman     : {}", event.completedAt());
        // Gerçek uygulamada burada email/SMS gönderilir
        // Şimdilik log'a yazıyoruz
        log.info("╔══════════════════════════════════════╗");
        log.info("║     ÖDEME BAŞARILI BİLDİRİMİ         ║");
        log.info("╠══════════════════════════════════════╣");
        log.info("║ Ödeme ID  : {}", event.paymentId());
        log.info("║ Hesap     : {}", event.sourceAccountId());
        log.info("║ Miktar    : {} {}", event.amount(), event.currency());
        log.info("║ Zaman     : {}", event.completedAt());
        log.info("╚══════════════════════════════════════╝");
    }

    public void sendPaymentFailedNotification(PaymentNotificationEvent event) {
        log.warn("╔══════════════════════════════════════╗");
        log.warn("║     ÖDEME BAŞARISIZ BİLDİRİMİ        ║");
        log.warn("╠══════════════════════════════════════╣");
        log.warn("║ Ödeme ID  : {}", event.paymentId());
        log.warn("║ Hesap     : {}", event.sourceAccountId());
        log.warn("║ Sebep     : {}", event.failureReason());
        log.warn("║ Zaman     : {}", event.completedAt());
        log.warn("╚══════════════════════════════════════╝");
    }
}
