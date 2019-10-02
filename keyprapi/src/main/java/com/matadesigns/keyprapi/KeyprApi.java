package com.matadesigns.keyprapi;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.matadesigns.keyprapi.environments.KeyprApiEnvironment;
import com.matadesigns.keyprapi.models.OAuthResponse;
import com.matadesigns.keyprapi.models.PagedReservations;
import com.matadesigns.keyprapi.models.Reservation;
import com.matadesigns.keyprapi.models.ReservationFolio;
import com.matadesigns.keyprapi.models.ReservationTask;

import org.json.JSONException;

import java.io.IOException;
import java.util.concurrent.Semaphore;

public class KeyprApi implements JWTGenerator.JWTNeeded {
    private KeyprApiEnvironment env;
    private JWTGenerator jwtGenerator;
    private WebToken jwt = new WebToken();
    private WebToken accessToken = new WebToken();
    private KeyprRequestBuilder builder;
    private Semaphore semaphore = new Semaphore(0);

    public KeyprApi(KeyprApiEnvironment env, JWTGenerator jwtGenerator) {
        this.env = env;
        this.jwtGenerator = jwtGenerator;
        this.builder = new KeyprRequestBuilder(env);
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
                    this.jwtGenerator.jwtTokenNeeded(this);
                    this.semaphore.acquire();
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

    public void getReservations(String query, Handler<PagedReservations> handler) {
        new Thread(() -> {
            if(checkAuthentication()) {
                try {
                    Gson gson = new GsonBuilder()
                            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                            .create();
                    PagedReservations reservations =  builder.reservations(query).perform(gson);
                    handler.onSuccess(reservations);
                } catch (Exception e) {
                    handler.onError(e);
                }
            }
        }).start();
    }

    public void getFolio(Reservation reservation, Handler<ReservationFolio> handler) {
        new Thread(() -> {
            if(checkAuthentication()) {
                try {
                    Gson gson = new GsonBuilder()
                            .setDateFormat("yyyy-MM-dd")
                            .create();
                    ReservationFolio folio = builder.folio(reservation.meta.folioDetailsUrl).perform(gson);
                    handler.onSuccess(folio);
                } catch (Exception e) {
                    handler.onError(e);
                }
            }
        }).start();
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

    @Override
    public void onComplete(String jwtValue) throws JWTExceptions.JWTInvalidPayloadException, JSONException, JWTExceptions.JWTInvalidStructureException {
        this.jwt.fill(jwtValue);
        this.semaphore.release();
    }

    @Override
    public void onFailure(Exception err) {
        this.semaphore.release();
        err.printStackTrace();
    }
}
