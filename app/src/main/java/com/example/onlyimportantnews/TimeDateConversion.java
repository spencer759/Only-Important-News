package com.example.onlyimportantnews;

import java.text.SimpleDateFormat;

public class TimeDateConversion {
    public static String unixToDayAndMonth(long unixTime) {
        java.util.Date date = new java.util.Date(unixTime*1000L);
        return new SimpleDateFormat("h").format(date);
    }

    public static String daysBetweenTimeAndNow(long newsItemUnixTime) {
        long currentUnixTime = System.currentTimeMillis() / 1000L;
        long unixTimeInBetween = currentUnixTime - newsItemUnixTime;
        long daysInBetween = unixTimeInBetween/60/60/24;
        return Long.toString(daysInBetween);
    }

    public static String hoursBetweenTimeAndNow(long newsItemUnixTime) {
        long currentUnixTime = System.currentTimeMillis() / 1000L;
        long unixTimeInBetween = currentUnixTime - newsItemUnixTime;
        long hoursInBetween = unixTimeInBetween/60/60;
        return Long.toString(hoursInBetween);
    }

    public static String unixTimeToDaysAndHours(long newsItemUnixTime) {
        long currentUnixTime = System.currentTimeMillis() / 1000L;
        long unixTimeInBetween = currentUnixTime - newsItemUnixTime;
        long hoursInBetween = unixTimeInBetween/60/60;
        long hours = hoursInBetween%24;
        long days = (hoursInBetween-hours)/24;

        if (days == 0) {
            return hours + " H";
        } else {
            return days + " D " + hours + " H";
        }
    }

}
