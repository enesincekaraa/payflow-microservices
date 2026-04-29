package com.payflow.paymentservice.infrastructure.iyzico;

import com.iyzipay.Options;
import com.iyzipay.model.*;
import com.iyzipay.request.CreatePaymentRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class IyzicoPaymentService {
    private final Options iyzicoOptions;

    public IyzicoPaymentService(Options iyzicoOptions) {
        this.iyzicoOptions = iyzicoOptions;
    }

    public IyzicoPaymentResult charge(
            String paymentId,
            BigDecimal amount,
            String currency,
            CardInfo cardInfo,
            BuyerInfo buyerInfo){

        log.info("API Key: {}, Base URL: {}",
                iyzicoOptions.getApiKey(),
                iyzicoOptions.getBaseUrl());
        try {
            CreatePaymentRequest req = new CreatePaymentRequest();
            req.setLocale(Locale.TR.getValue());
            req.setConversationId(paymentId);
            req.setPrice(amount.setScale(2, RoundingMode.HALF_UP));
            req.setPaidPrice(amount.setScale(2, RoundingMode.HALF_UP));
            req.setCurrency(Currency.TRY.name());
            req.setInstallment(1);
            req.setBasketId(paymentId);
            req.setPaymentChannel(PaymentChannel.WEB.name());
            req.setPaymentGroup(PaymentGroup.PRODUCT.name());

            // Kart bilgileri
            PaymentCard paymentCard = new PaymentCard();
            paymentCard.setCardHolderName(cardInfo.cardHolderName());
            paymentCard.setCardNumber(cardInfo.cardNumber());
            paymentCard.setExpireMonth(cardInfo.expireMonth());
            paymentCard.setExpireYear(cardInfo.expireYear());
            paymentCard.setCvc(cardInfo.cvc());
            paymentCard.setRegisterCard(0);
            req.setPaymentCard(paymentCard);

            // Alıcı bilgileri
            Buyer buyer = new Buyer();
            buyer.setId(buyerInfo.buyerId());
            buyer.setName(buyerInfo.name());
            buyer.setSurname(buyerInfo.surname());
            buyer.setEmail(buyerInfo.email());
            buyer.setIdentityNumber("11111111111"); // sandbox için sabit
            buyer.setRegistrationAddress("Test Adres");
            buyer.setCity("Istanbul");
            buyer.setCountry("Turkey");
            buyer.setIp("85.34.78.112"); // sandbox için sabit
            req.setBuyer(buyer);


            // Fatura adresi
            Address shippingAddress = new Address();
            shippingAddress.setContactName(buyerInfo.name() + " " + buyerInfo.surname());
            shippingAddress.setCity("Istanbul");
            shippingAddress.setCountry("Turkey");
            shippingAddress.setAddress("Test Adres");
            req.setShippingAddress(shippingAddress);
            req.setBillingAddress(shippingAddress);



            // Sepet
            List<BasketItem> basketItems = new ArrayList<>();
            BasketItem item = new BasketItem();
            item.setId(paymentId);
            item.setName("PayFlow Transfer");
            item.setCategory1("Transfer");
            item.setItemType(BasketItemType.VIRTUAL.name());
            item.setPrice(amount.setScale(2, RoundingMode.HALF_UP));

            basketItems.add(item);
            req.setBasketItems(basketItems);


            Payment payment = Payment.create(req,iyzicoOptions);

            log.info("iyzico sonucu: {} | conversationId: {}",
                    payment.getStatus(), paymentId);

            if ("success".equals(payment.getStatus())) {
                return new IyzicoPaymentResult(
                        true,
                        payment.getPaymentId(),
                        null
                );

            }else {
                return new IyzicoPaymentResult(
                        false,
                        null,
                        payment.getErrorMessage()
                );
            }

        }catch (Exception e){
            log.error("iyzico hatası: {}", e.getMessage());
            return new IyzicoPaymentResult(false, null, e.getMessage());
        }
    }




    public record IyzicoPaymentResult(
            boolean success,
            String iyzicoPaymentId,
            String errorMessage
    ){}

    public record CardInfo(
            String cardHolderName,
            String cardNumber,
            String expireMonth,
            String expireYear,
            String cvc
    ) {}

    public record BuyerInfo(
            String buyerId,
            String name,
            String surname,
            String email
    ) {}
}

