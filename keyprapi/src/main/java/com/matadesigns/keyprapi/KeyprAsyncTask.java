package com.matadesigns.keyprapi;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public enum KeyprAsyncTask implements Serializable {
    @SerializedName("check_in") CHECK_IN,
    @SerializedName("check_out") CHECK_OUT
}
