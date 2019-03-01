package com.matadesigns.keyprapi.environments;

import androidx.annotation.NonNull;

public interface KeyprApiEnvironment {
    @NonNull
    String getApiUrl();
    @NonNull
    String getAccountUrl();
};
