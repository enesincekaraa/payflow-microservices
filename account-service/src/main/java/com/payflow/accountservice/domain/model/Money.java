package com.payflow.accountservice.domain.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;

public record Money(BigDecimal amount, Currency currency) {

    public Money {
        if (amount == null) throw new IllegalArgumentException("Miktar null olamaz");
        if (currency == null) throw new IllegalArgumentException("Para birimi null olamaz");
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Miktar negatif olamaz");
        }
        amount = amount.setScale(2, RoundingMode.HALF_UP);
    }


    public static Money of(BigDecimal amount, String currencyCode) {
        return new Money(amount, Currency.getInstance(currencyCode));
    }
    public static Money ofTRY(BigDecimal amount) {
        return new Money(amount, Currency.getInstance("TRY"));
    }
    public static Money zero(String currencyCode) {
        return new Money(BigDecimal.ZERO, Currency.getInstance(currencyCode));
    }

    public Money add(Money other) {
        validateSameCurrency(other);
        return new Money((this.amount.add(other.amount)), this.currency);
    }

    public Money subtract(Money other) {
        validateSameCurrency(other);
        BigDecimal result = this.amount.subtract(other.amount);
        if (result.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Yetersiz bakiye");
        }
        return new Money(result, this.currency);
    }

    public boolean isGreaterThan(Money other) {
        validateSameCurrency(other);
        return this.amount.compareTo(other.amount) > 0;
    }

    public boolean isZero() {
        return this.amount.compareTo(BigDecimal.ZERO) == 0;
    }

    private void validateSameCurrency(Money other) {
        if (!this.currency.equals(other.currency)) {
            throw new IllegalArgumentException(
                    "Para birimleri eşleşmiyor: " + this.currency + " vs " + other.currency
            );
        }
    }
    @Override
    public String toString() {
        return amount.toPlainString() + " " + currency.getCurrencyCode();
    }

}
