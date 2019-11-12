package com.example.onlyimportantnews;

import java.text.SimpleDateFormat;

public class TimeDateConversion {
    public static String UnixToDayAndMonth(long unixTime) {
        java.util.Date date = new java.util.Date(unixTime*1000L);
        return new SimpleDateFormat("dd MMM").format(date);
    }

}
