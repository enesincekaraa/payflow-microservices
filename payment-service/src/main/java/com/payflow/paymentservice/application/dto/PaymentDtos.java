package com.payflow.paymentservice.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PaymentDtos {

//    public record InitiatePaymentRequest(
//            @NotBlank(message = "Kaynak hesap ID zorunlu")
//            String sourceAccountId,
//
//            @NotBlank(message = "Hedef hesap ID zorunlu")
//            String targetAccountId,
//
//            @NotNull @Positive(message = "Miktar pozitif olmalı")
//            BigDecimal amount,
//
//            @NotBlank(message = "Para birimi zorunlu")
//            String currency,
//
//            String description
//    ) {}

    public record PaymentResponse(
            String id,
            String sourceAccountId,
            String targetAccountId,
            BigDecimal amount,
            String currency,
            String status,
            String failureReason,
            String description,
            LocalDateTime createdAt
    ) {}


    public record InitiatePaymentRequest(
            @NotBlank(message = "Kaynak hesap ID zorunlu")
            String sourceAccountId,

            @NotBlank(message = "Hedef hesap ID zorunlu")
            String targetAccountId,

            @NotNull @Positive(message = "Miktar pozitif olmalı")
            BigDecimal amount,

            @NotBlank(message = "Para birimi zorunlu")
            String currency,

            String description,

            // Kart bilgileri — iyzico için
            @NotNull
            CardDetails cardDetails,

            // Alıcı bilgileri — iyzico için
            @NotNull
            BuyerDetails buyerDetails
    ) {}

    public record CardDetails(
            @NotBlank String cardHolderName,
            @NotBlank String cardNumber,
            @NotBlank String expireMonth,
            @NotBlank String expireYear,
            @NotBlank String cvc
    ) {}

    public record BuyerDetails(
            @NotBlank String buyerId,
            @NotBlank String name,
            @NotBlank String surname,
            @NotBlank String email
    ) {}
}