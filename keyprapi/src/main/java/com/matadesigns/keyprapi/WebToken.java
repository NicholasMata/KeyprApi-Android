package com.matadesigns.keyprapi;

import android.util.Base64;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Date;

public class WebToken {
    public String value;
    public Date expiration;

    public WebToken() {}

    public void expiresOn(long expirationUnix) {
        this.expiration = new Date(expirationUnix*1000);
    }

    public void expiresIn(int expirationIn) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.SECOND, expirationIn);
        this.expiration = calendar.getTime();
    }

    public boolean isExpired() {
        return this.expiration.compareTo(new Date()) < 0;
    }

    public boolean isValid() {
        return this.value != null && !isExpired();
    }

    public void fill(String jwt) throws JWTExceptions.JWTInvalidStructureException, JWTExceptions.JWTInvalidPayloadException, JSONException {
        String[] jwtComponents = jwt.split("\\.");
        if (jwtComponents.length != 3) {
            throw new JWTExceptions.JWTInvalidStructureException("JWT contains "+jwtComponents.length+" components.");
        }
        String encodedPayload = jwtComponents[1];
        try {
            byte[] payloadData = Base64.decode(encodedPayload, Base64.DEFAULT);
            String payload = new String(payloadData, "UTF-8");
            JSONObject obj = new JSONObject(payload);
            Double expiration = obj.getDouble("exp");
            this.expiresOn(expiration.longValue());
        } catch (UnsupportedEncodingException e) {
            throw new JWTExceptions.JWTInvalidPayloadException("Payload not properly base64 encoded.");
        }
        this.value = jwt;
    }
}
