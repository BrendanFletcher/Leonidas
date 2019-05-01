package com.endeavour.saddam.pharmacyautomation.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class Constants {

    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
    public static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("hh:mm aaa E MMMM d, yyyy", Locale.ENGLISH);
    public static final DateFormatSymbols FORMAT_SYMBOLS = new DateFormatSymbols();
    public static final Type LIST_STRING_TYPE = new TypeToken<List<String>>() {}.getType();
    public static final Gson GSON = new Gson();
    public static final int WARNING_TYPE = 1;
    public static final int NORMAL_TYPE = 0;
    public static final int HOME_TYPE = 3;
    public static final int AVAILABLE_TYPE = 4;
    public static final int EXPIRED_TYPE = 5;
    public static final int SOLD_OUT_TYPE = 6;
    public static final int WISHLIST_TYPE = 7;
    public static final int CALCULATION_TYPE = 8;
    public static int TOTAL_PRICE = 0;



    public class Request {
        public static final int NEW_MEDICINE = 1;
    }

    public class Key {
        public static final String ID = "id";
        public static final String MEDICINE = "medicine";
        public static final String LOCATION = "location";
    }
}
