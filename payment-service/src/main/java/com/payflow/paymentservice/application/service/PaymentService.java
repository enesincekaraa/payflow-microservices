package com.payflow.paymentservice.application.service;

import com.payflow.paymentservice.application.dto.PaymentDtos;
import com.payflow.paymentservice.domain.event.PaymentEvent;
import com.payflow.paymentservice.domain.exception.PaymentException;
import com.payflow.paymentservice.domain.model.Payment;
import com.payflow.paymentservice.infrastructure.kafka.producer.PaymentEventProducer;
import com.payflow.paymentservice.infrastructure.repository.PaymentRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final PaymentEventProducer eventProducer;

    public PaymentService(PaymentRepository paymentRepository, PaymentEventProducer eventProducer) {
        this.paymentRepository = paymentRepository;
        this.eventProducer = eventProducer;
    }

    @Transactional
    public PaymentDtos.PaymentResponse initiatePayment(PaymentDtos.InitiatePaymentRequest req){
        Payment payment = Payment.create(
                req.sourceAccountId(),
                req.targetAccountId(),
                req.amount(),
                req.currency(),
                req.description()
        );

        Payment saved = paymentRepository.save(payment);
        log.info("Ödeme oluşturuldu: {} | PENDING", saved.getId());

        eventProducer.sendInitiated(new PaymentEvent.PaymentInitiated(
                saved.getId(),
                saved.getSourceAccountId(),
                saved.getTargetAccountId(),
                saved.getAmount(),
                saved.getCurrency(),
                saved.getDescription(),
                LocalDateTime.now()
        ));
        return toResponse(saved);
    }

    @Transactional
    public void completePayment(String paymentId){
        Payment payment = findById(paymentId);
        payment.complete();
        paymentRepository.save(payment);
        log.info("Ödeme tamamlandı: {}", paymentId);


        eventProducer.sendPaymentCompleted(new PaymentEvent.PaymentCompleted(
                payment.getId(),
                payment.getSourceAccountId(),
                payment.getAmount(),
                payment.getCurrency(),
                LocalDateTime.now()
        ));
    }

    @Transactional(readOnly = true)
    public PaymentDtos.PaymentResponse getPayment(String paymentId) {
        return toResponse(findById(paymentId));
    }

    @Transactional(readOnly = true)
    public List<PaymentDtos.PaymentResponse> getPaymentsByAccount(String accountId) {
        return paymentRepository.findBySourceAccountId(accountId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public void failPayment(String paymentId, String reason) {
        Payment payment = findById(paymentId);
        payment.fail(reason);
        paymentRepository.save(payment);

        log.warn("Ödeme başarısız: {} | sebep: {}", paymentId, reason);

        eventProducer.sendPaymentFailed(new PaymentEvent.PaymentFailed(
                payment.getId(),
                reason,
                LocalDateTime.now()
        ));
    }

    private Payment findById(String paymentId) {
        return paymentRepository.findById(paymentId).orElseThrow(
                ()-> new PaymentException.NotFound(paymentId)
        );
    }


    private PaymentDtos.PaymentResponse toResponse(Payment payment){
        return new PaymentDtos.PaymentResponse(
                payment.getId(),
                payment.getSourceAccountId(),
                payment.getTargetAccountId(),
                payment.getAmount(),
                payment.getCurrency(),
                payment.getStatus().name(),
                payment.getFailureReason(),
                payment.getDescription(),
                payment.getCreatedAt()
        );
    }




}
