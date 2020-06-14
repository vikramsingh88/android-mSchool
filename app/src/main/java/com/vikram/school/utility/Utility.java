package com.vikram.school.utility;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Utility {
    private static String TAG = "Utility";
    public static String datePattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    public static String formatDate(String date, String pattern) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("IST"));
        try {
            Date d = simpleDateFormat.parse(date);
            simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
            String str = simpleDateFormat.format(d);
            return str;
        } catch (Exception e) {
            e.printStackTrace();
            return date;
        }
    }

    public static String formatDate(String strDate) {
        Log.d(Constants.TAG, TAG+" String date "+strDate);
        String str = strDate;
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            Date date = formatter.parse(strDate);
            formatter = new SimpleDateFormat("MMMM yyyy");
            str = formatter.format(date);
        } catch(Exception e) {
            e.printStackTrace();
        }
        Log.d(Constants.TAG, TAG+" formatted date "+str);
        return str;
    }

    public static String formatDate(Date date) {
        Log.d(Constants.TAG, TAG+" String date "+date);
        String str = "";
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
            str = formatter.format(date);
        } catch(Exception e) {
            e.printStackTrace();
        }
        Log.d(Constants.TAG, TAG+" formatted date "+str);
        return str;
    }
}
