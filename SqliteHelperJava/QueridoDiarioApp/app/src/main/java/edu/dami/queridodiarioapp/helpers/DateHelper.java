package edu.dami.queridodiarioapp.helpers;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateHelper {

    public static final SimpleDateFormat defaultDateTimeFormatter =
            new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ROOT); // 26/11/2020 02:00 pm

    public static String getDateFromMilli(long milli) {
        try {
            return getDateFromMilliOrThrow(milli);
        } catch (Exception ex) {
            Log.e("DateHelper", ex.getMessage());
            return null;
        }
    }

    public static String getDateFromMilliOrThrow(long milli) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milli);
        return defaultDateTimeFormatter.format(calendar.getTime());
    }

    public static long getNowMillis() {
        return new Date().getTime();
    }
}
