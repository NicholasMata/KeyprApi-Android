package com.matadesigns.keyprapi;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class Request<T extends Serializable> {
    public enum Method {
        Get, Post, Put, Delete;

        @Override
        public String toString() {
            switch (this) {
                case Get: return "GET";
                case Post: return "POST";
                case Put: return "PUT";
                case Delete: return "DELETE";
                default: return "GET";
            }
        }
    }
    private HttpURLConnection con;
    private Class<T> responseType;
    public Request(Method method, URL url, String body, Class<T> responseType, Map<String, String> headers) throws IOException {
        this.responseType = responseType;
        con = (HttpURLConnection) url.openConnection();
        for (Map.Entry<String, String> entry:
             headers.entrySet()) {
            con.addRequestProperty(entry.getKey(), entry.getValue());
        }
        con.addRequestProperty("Content-Type", "application/json");
        con.setRequestMethod(method.toString());
        if (body != null) {
            OutputStream out = con.getOutputStream();
            out.write(body.getBytes("UTF-8"));
            out.close();
        }
    }
    public Request(Method method, URL url, String body, Class<T> responseType) throws IOException {
        this(method, url, body, responseType, new HashMap<>());
    }

    public T perform() throws Exception {
        return this.perform(new Gson());
    }

    public T perform(Gson gson) throws Exception {
        int responseCode = con.getResponseCode();
        if(responseCode >=200 && responseCode < 300) {
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            return gson.fromJson(response.toString(), responseType);
        } else {

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getErrorStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            throw new Exception(response.toString());
        }
    }
}
