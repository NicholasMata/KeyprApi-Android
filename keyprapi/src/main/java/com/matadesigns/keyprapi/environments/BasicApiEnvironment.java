package com.matadesigns.keyprapi.environments;

import androidx.annotation.NonNull;

public class BasicApiEnvironment implements KeyprApiEnvironment {

    public static KeyprApiEnvironment production = new BasicApiEnvironment("https://api.keypr.com","https://account.keypr.com");
    public static KeyprApiEnvironment staging = new BasicApiEnvironment("https://api.keyprstg.com","https://account.keyprstg.com");

    public String apiUrl;
    public String accountsUrl;

    public BasicApiEnvironment(String apiUrl, String accountsUrl) {
        this.apiUrl = apiUrl;
        this.accountsUrl = accountsUrl;
    }

    @NonNull
    @Override
    public String getApiUrl() {
        return apiUrl;
    }

    @NonNull
    @Override
    public String getAccountUrl() {
        return accountsUrl;
    }
}