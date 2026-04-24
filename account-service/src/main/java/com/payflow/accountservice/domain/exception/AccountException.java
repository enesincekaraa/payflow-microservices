package com.payflow.accountservice.domain.exception;

import com.payflow.accountservice.domain.model.Money;

public abstract sealed class AccountException extends RuntimeException
        permits AccountException.NotFound,
        AccountException.InsufficientFunds,
        AccountException.AccountInactive,
        AccountException.DuplicateAccount {

    protected AccountException(String message) {
        super(message);
    }

    public static final class NotFound extends AccountException {
        public NotFound(String accountId) {
            super("Hesap bulunamadı: " + accountId);
        }
    }

    public static final class InsufficientFunds extends AccountException {
        public InsufficientFunds(Money available, Money requested) {
            super("Yetersiz bakiye. Mevcut: " + available + ", İstenen: " + requested);
        }
    }

    public static final class AccountInactive extends AccountException {
        public AccountInactive(String accountId) {
            super("Hesap aktif değil: " + accountId);
        }
    }

    public static final class DuplicateAccount extends AccountException {
        public DuplicateAccount(String ownerId) {
            super("Bu kullanıcının zaten hesabı var: " + ownerId);
        }
    }
}