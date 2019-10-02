package com.matadesigns.keyprapi;

import org.json.JSONException;

import java.util.function.Function;

public interface JWTGenerator {
    public interface JWTNeeded {
        void onComplete(String jwt) throws JWTExceptions.JWTInvalidPayloadException, JSONException, JWTExceptions.JWTInvalidStructureException;
        void onFailure(Exception err);
    }
    void jwtTokenNeeded(JWTNeeded handler);
}
