package com.matadesigns.keyprapi.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ReservationTask implements Serializable {
    public Task data;

    public enum Status implements Serializable {
        PENDING, SUCCESS, FAILURE
    }

    public class Task implements Serializable {
        @SerializedName("id")
        public String id;
        @SerializedName("type")
        public String type;
        @SerializedName("attributes")
        public Attributes attributes;


        public class Attributes implements Serializable {
            @SerializedName("name")
            public String name;
            @SerializedName("status")
            public String status;
            @SerializedName("result")
            public String[] result;
            @SerializedName("ready")
            public boolean ready;
            @SerializedName("successful")
            public boolean successful;
            @SerializedName("failed")
            public boolean failed;
            @SerializedName("status_url")
            public String statusUrl;
        }
    }
}
