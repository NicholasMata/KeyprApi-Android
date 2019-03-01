package com.matadesigns.keyprapi;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.matadesigns.keyprapi.environments.KeyprApiEnvironment;
import com.matadesigns.keyprapi.models.OAuthResponse;
import com.matadesigns.keyprapi.models.PagedReservations;
import com.matadesigns.keyprapi.models.Reservation;
import com.matadesigns.keyprapi.models.ReservationTask;

import org.json.JSONException;

import java.io.IOException;

public class KeyprApi {
    private KeyprApiEnvironment env;
    private JWTGenerator jwtGenerator;
    private WebToken jwt = new WebToken();
    private WebToken accessToken = new WebToken();
    private KeyprRequestBuilder builder;
    private RequestQueue queue;

    public KeyprApi(KeyprApiEnvironment env, JWTGenerator jwtGenerator) {
        this.env = env;
        this.jwtGenerator = jwtGenerator;
        this.builder = new KeyprRequestBuilder(env);
        this.queue = new RequestQueue("com.matadesigns.keyprapi");
    }

    public WebToken getAccessToken() {
        return accessToken;
    }

    public KeyprApiEnvironment getEnv() {
        return env;
    }

    public void setEnv(KeyprApiEnvironment env) {
        this.env = env;
        this.builder = new KeyprRequestBuilder(env);
    }

    public void clearTokens() {
        this.accessToken = new WebToken();
        this.jwt = new WebToken();
    }

    public boolean checkAuthentication() {
        if (!accessToken.isValid()) {
            if (!jwt.isValid()) {
                try {
                    this.jwt.fill(this.jwtGenerator.jwtTokenNeeded());
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            }
            try {
                OAuthResponse oauthRes = builder.federated(jwt.value)
                                                .perform();
                this.accessToken.value = oauthRes.accessToken;
                this.accessToken.expiresIn(oauthRes.expiresIn);
                builder.headers.put("Authorization", "Bearer "+this.accessToken.value);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    public PagedReservations getReservations(String query) {
        if(checkAuthentication()) {
            try {
                Gson gson = new GsonBuilder()
                        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                        .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                        .create();
                return builder.reservations(query).perform(gson);
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    public void perform(KeyprAsyncTask task, String reservationId, int timeout, int interval, Handler<ReservationTask> handler) {
        if(checkAuthentication()) {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    try {
                        Request<ReservationTask> requestTask = builder.perfom(task,reservationId, timeout, interval);
                        ReservationTask reservationTask = requestTask.perform();

                        Runnable r = new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Request<ReservationTask> checkRequest = builder.check(reservationTask.data.id);
                                    ReservationTask checkTask = checkRequest.perform();

                                    if(checkTask.data.attributes.failed) {
                                        handler.onError(new Exception("Check in failed try again later."));
                                        return;
                                    }

                                    if (checkTask.data.attributes.successful) {
                                        handler.onSuccess(checkTask);
                                        return;
                                    }
                                    Thread.sleep(interval);
                                    this.run();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    handler.onError(e);
                                }
                            }
                        };
                        r.run();
                    } catch(Exception e) {
                        e.printStackTrace();
                        handler.onError(e);
                    }
                }
            };
            new Thread(runnable).start();
        }
    }

}
