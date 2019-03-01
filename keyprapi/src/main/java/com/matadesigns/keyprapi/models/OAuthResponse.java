package com.matadesigns.keyprapi.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class OAuthResponse implements Serializable {
    @SerializedName("access_token")
    public String accessToken;
    @SerializedName("expires_in")
    public int expiresIn;
    @SerializedName("token_type")
    public String tokenType;
    @SerializedName("scope")
    public String scope;
    @SerializedName("refresh_token")
    public String refreshToken;
    @SerializedName("id_token")
    public String idToken;
}
