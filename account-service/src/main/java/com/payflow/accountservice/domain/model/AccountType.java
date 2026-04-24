package com.payflow.accountservice.domain.model;

public sealed interface AccountType
        permits AccountType.Personal,
        AccountType.Business,
        AccountType.Savings {
    record Personal(String owerName) implements AccountType {}
    record Business(String companyName,String taxId) implements AccountType {}
    record Savings(String owerName,double interestRate) implements AccountType {}
}
