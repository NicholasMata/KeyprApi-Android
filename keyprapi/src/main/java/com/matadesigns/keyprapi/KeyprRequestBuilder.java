package com.matadesigns.keyprapi;

import com.google.gson.reflect.TypeToken;
import com.matadesigns.keyprapi.environments.KeyprApiEnvironment;
import com.matadesigns.keyprapi.models.OAuthResponse;
import com.matadesigns.keyprapi.models.PagedReservations;
import com.matadesigns.keyprapi.models.Reservation;
import com.matadesigns.keyprapi.models.ReservationTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class KeyprRequestBuilder {
    private KeyprApiEnvironment env;
    public Map<String, String> headers = new HashMap<>();

    public KeyprRequestBuilder(KeyprApiEnvironment env) {
        this.env = env;
    }

    private URL buildUrl(String baseUrl, String uri) throws MalformedURLException {
        URL url = new URL(baseUrl+uri);
        System.out.println("Built Url: "+url.toString());
        return url;
    }

    public Request<OAuthResponse> federated(String jwt) throws JSONException, IOException {
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("jwt", jwt);
            URL url = buildUrl(env.getAccountUrl(), "/federated-id/token");
            return new Request<OAuthResponse>(Request.Method.Post, url, jsonBody.toString(), OAuthResponse.class, headers);
    }

    public Request<PagedReservations> reservations(String query) throws IOException {
        URL url = buildUrl(env.getApiUrl(), "/v1/reservations"+query);
        return new Request<PagedReservations>(Request.Method.Get,
                url, null, PagedReservations.class, headers);
    }

    public Request<ReservationTask> perfom(KeyprAsyncTask task, String reservationId, int timeout, int interval) throws IOException  {
        URL url = buildUrl(env.getApiUrl(), "/v1/reservations/"+reservationId+"/async_"+ task.toString().toLowerCase());
        return new Request<ReservationTask>(Request.Method.Put,
                url, null, ReservationTask.class, headers);
    }

    public Request<ReservationTask> check(String taskId) throws IOException  {
        URL url = buildUrl(env.getApiUrl(), "/v1/tasks/"+taskId);
        return new Request<ReservationTask>(Request.Method.Get,
                url, null, ReservationTask.class, headers);
    }
}
