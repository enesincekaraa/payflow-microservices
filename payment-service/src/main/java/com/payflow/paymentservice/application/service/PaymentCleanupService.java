package com.payflow.paymentservice.application.service;

import com.payflow.paymentservice.domain.model.Payment;
import com.payflow.paymentservice.domain.model.PaymentStatus;
import com.payflow.paymentservice.infrastructure.repository.PaymentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class PaymentCleanupService {

    private final PaymentService paymentService;
    private final PaymentRepository paymentRepository;

    public PaymentCleanupService(PaymentService paymentService, PaymentRepository paymentRepository) {
        this.paymentService = paymentService;
        this.paymentRepository = paymentRepository;
    }

    @Value("${scheduling.pending-payment-timeout:30}")
    private int timeoutMinutes;


    @Scheduled(fixedDelayString = "${scheduling.cleanup.fixed-delay:300000}")
    @Transactional
    public void cleanupStalePendingPayments(){
        LocalDateTime cutoff = LocalDateTime.now().minusMinutes(timeoutMinutes);

        List<Payment> stalePayments = paymentRepository.findByStatusAndCreatedAtBefore(
                PaymentStatus.PENDING,cutoff);

        if (stalePayments.isEmpty()){
            log.debug("Temizlenecek bekleyen ödeme yok.");
            return;
        }
        log.warn("🧹 {} adet takılı ödeme temizleniyor...",
                stalePayments.size());

        stalePayments.forEach(payment -> {
            log.warn("⏰ Zaman aşımı: {} | oluşturulma: {}",
                    payment.getId(), payment.getCreatedAt());
            paymentService.failPayment(
                    payment.getId(),
                    "Zaman aşımı -" +timeoutMinutes + "dakika içinde tamamlanamadı");
        });

        log.info("✅ {} ödeme temizlendi.", stalePayments.size());

    }
}
