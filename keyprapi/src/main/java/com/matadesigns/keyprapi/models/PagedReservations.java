package com.matadesigns.keyprapi.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class PagedReservations implements Serializable {
    @SerializedName("data")
    public List<Reservation> data;
    @SerializedName("links")
    public PagingInfo pagingInfo;
    public List<AdditionalInfo> included;

    public class PagingInfo implements Serializable {
        @SerializedName("first")
        public String first;
        @SerializedName("next")
        public String next;
        @SerializedName("prev")
        public String prev;
    }

    public class AdditionalInfo implements Serializable {
        public String id;
        public String type;
        public Attributes attributes;

        public class Attributes implements Serializable{
            public String name;
            public String locationStatus;
            public String externalId;
        }
    }
}
