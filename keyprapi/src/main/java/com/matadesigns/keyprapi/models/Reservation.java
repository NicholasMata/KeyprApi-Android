package com.matadesigns.keyprapi.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Reservation implements Serializable {
    public String id;
    public String type;
    public Attributes attributes;
    public Relationships relationships;
    public Metadata meta;

    public class Metadata implements Serializable{
        public String folioDetailsUrl;
        public boolean canCheckIn;
        public boolean canCheckOut;
        public boolean isDueIn;
        public boolean isDueOut;
        public boolean isAssigned;
        public boolean canPreCheckIn;
    }

    public class Relationships implements  Serializable {
        public RelationshipData users;
        public RelationshipData locations;

        public class RelationshipData implements Serializable {
            public List<Relationship> data;
        }

        public class Relationship implements  Serializable {
            public String id;
            public String type;
        }
    }

    public enum State {
        @SerializedName("pending")
        pending,
        @SerializedName("reserved")
        reserved,
        @SerializedName("checked_in")
        checkedIn,
        @SerializedName("checked_out")
        checkedOut,
        @SerializedName("archived")
        archived,
        @SerializedName("canceled")
        canceled
    }

    public class Attributes implements Serializable {
        public State state;
        public String externalId;
        public String confirmationId;
        public String firstName;
        public String lastName;
        public String middleName;
        public String email;
        public String phone;
        public String address;
        public String postalCode;
        public String company;
        public MetaFields metaFields;
        @SerializedName("checkin_date")
        public Date checkInDate;
        @SerializedName("checkout_date")
        public Date checkOutDate;

        public class MetaFields implements Serializable {
            @SerializedName("echeckoutEnabled")
            public boolean eCheckOutEnabled;
            @SerializedName("echeckinEnabled")
            public boolean eCheckInEnabled;

        }
    }
}
