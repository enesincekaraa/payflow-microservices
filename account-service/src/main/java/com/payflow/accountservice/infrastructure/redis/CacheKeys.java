package com.payflow.accountservice.infrastructure.redis;

public final class CacheKeys {
    private CacheKeys() {
    }

    public static final String ACCOUNT = "account";
    public static final String ACCOUNT_LIST = "account_list";
    public static final String BALANCE = "balance";

    public static String accountKey(String accountId) {
        return ACCOUNT + "::" + accountId;
    }
    public static String balanceKey(String accountId) {
        return BALANCE + "::" + accountId;
    }

    public static String ownerKey(String ownerId) {
        return ACCOUNT_LIST + "::" + ownerId;
    }

}
