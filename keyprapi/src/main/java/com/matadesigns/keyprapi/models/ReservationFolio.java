package com.matadesigns.keyprapi.models;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class ReservationFolio implements Serializable {
    public List<FolioBill> bills;
    public class FolioBill implements  Serializable {
        public List<FolioBillItems> billItems;
        public FolioBalance currentBalance;

        public class FolioBalance implements  Serializable {
            public Float amount;
            public String currencyCode;
        }
        public class FolioBillItems implements Serializable {
            public FolioBalance amount;
            public Date date;
            public String description;
        }
    }
}
