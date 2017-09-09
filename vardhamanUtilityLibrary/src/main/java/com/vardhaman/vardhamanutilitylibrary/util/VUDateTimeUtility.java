package com.vardhaman.vardhamanutilitylibrary.util;

import android.content.Context;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class VUDateTimeUtility {

    private static VUDateTimeUtility vuDateTimeUtility;
    private Context context;
    public static final int TIME_FORMAT_DEFAULT = 0, TIME_FORMAT_DATE = 1, TIME_FORMAT_READABLE_DATE = 2,TIME_FORMAT_TIME_ONLY = 3, TIME_FORMAT_DATE_INVERSE = 4, TIME_FORMAT_DATE_2 = 5;

    private SimpleDateFormat defaultFormat = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss", Locale.getDefault()), dateOnlyFormat = new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault())
            ,timeOnlyFormat = new SimpleDateFormat("HH:mm",Locale.getDefault()),readableDateTime = new SimpleDateFormat("dd MMM, yyyy HH:mma"),
    dateOnlyFormatInverse = new SimpleDateFormat("yyyy-MM-dd"), dateOnlyFormat2 = new SimpleDateFormat("dd.MM.yyyy");


    public static VUDateTimeUtility getInstance(Context context){
        if (vuDateTimeUtility == null)
        vuDateTimeUtility = new VUDateTimeUtility(context);

        return  vuDateTimeUtility;
    }

    VUDateTimeUtility(Context context){
        this.context = context;
    }


    public boolean validateDate(Calendar date, TimeZone tz){
        Calendar now = Calendar.getInstance(tz);
        return date.compareTo(now) == 1;
    }

    public String getCurrentTimeInUTC(int timeFormat) {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        return calendarToString(cal,timeFormat);
    }

    public String getCurrentTimeInLocal(int timeFormat) {
        return calendarToString(Calendar.getInstance(TimeZone.getDefault()),timeFormat);
    }

    public String calendarToString(Calendar cal,int timeFormat) {
        return getTimeFormat(timeFormat).format(cal.getTime());
    }



    public SimpleDateFormat getTimeFormat(int timeFormat){
        switch (timeFormat){
            case TIME_FORMAT_DATE:
                return dateOnlyFormat;
            case TIME_FORMAT_TIME_ONLY:
                return timeOnlyFormat;
            case TIME_FORMAT_READABLE_DATE:
                return readableDateTime;
            case TIME_FORMAT_DATE_INVERSE:
                return dateOnlyFormatInverse;
            case TIME_FORMAT_DATE_2:
                return dateOnlyFormat2;
            case TIME_FORMAT_DEFAULT:
            default:
                return defaultFormat;
        }
    }

    public Calendar stringToCalendar(String str, TimeZone tz, int timeFormat) {
        Date date = null;
        Calendar calendar = Calendar.getInstance(tz);
        try {
            date = getTimeFormat(timeFormat).parse(str);
            calendar.setTime(date);
            return calendar;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;

    }

    public String getLocalTimeFromUTCString(String time, int timeFormat)
            throws ParseException {
        SimpleDateFormat format = getTimeFormat(timeFormat);
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = format.parse(time);
        format.setTimeZone(TimeZone.getDefault());
        String formattedDate = format.format(date);
        return formattedDate;
    }

    public String getLocalTimeFromUTCString(String time, int timeFormat,int outTimeFormat)
            throws ParseException {
        SimpleDateFormat format = getTimeFormat(timeFormat);
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = format.parse(time);
        format = getTimeFormat(outTimeFormat);
        format.setTimeZone(TimeZone.getDefault());
        String formattedDate = format.format(date);
        return formattedDate;
    }

    public String getUTCFromLocalTimeString(String time, int timeFormat)
            throws ParseException {
        SimpleDateFormat format = getTimeFormat(timeFormat);
        format.setTimeZone(TimeZone.getDefault());
        Date date = format.parse(time);
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        String formattedDate = format.format(date);
        return formattedDate;
    }

    public Calendar getUTCFromLocalTime(Calendar time){
        SimpleDateFormat format = getTimeFormat(TIME_FORMAT_DEFAULT);
        format.setTimeZone(TimeZone.getDefault());
        Date date = time.getTime();
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        String formattedDate = format.format(date);
        return stringToCalendar(formattedDate,format.getTimeZone(),TIME_FORMAT_DEFAULT);
    }




    public String getReadableTimeDifference(long difference) {
        String timeago = "";
        try {
            // in milliseconds
            long diffSeconds = difference / 1000;
            long diffMinutes = diffSeconds / 60;
            long diffHours = diffMinutes / 60;
            long diffDays = difference / (24 * 60 * 60 * 1000);

            if (diffHours > 0) {
                if (diffHours == 1)
                    timeago = "1 hour";
                else if (diffHours < 24)
                    timeago = String.valueOf(diffHours) + " hours";
                else {
                    // int days = (int)Math.ceil(diffHours % 24);
                    if (diffDays == 1)
                        timeago = "1 day";
                    else
                        timeago = String.valueOf(diffDays) + " days";
                }
            } else {
                if (diffMinutes == 0)
                    timeago = "less than 1 minute";
                else if (diffMinutes == 1)
                    timeago = "1 minute";
                else
                    timeago = String.valueOf(diffMinutes) + " minutes";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return timeago;
    }

    public String getReadableDay(Calendar calendar, TimeZone tz){
        Calendar now = Calendar.getInstance(tz);
        if (now.get(Calendar.YEAR) == calendar.get(Calendar.YEAR) && now.get(Calendar.MONTH) == calendar.get(Calendar.MONTH)){
            if (now.get(Calendar.DAY_OF_MONTH) == calendar.get(Calendar.DAY_OF_MONTH)){
                return "Today";
            }else if (calendar.get(Calendar.DAY_OF_MONTH) - now.get(Calendar.DAY_OF_MONTH) == 1){
                return "Tomorrow";
            }else{
                return calendar.getDisplayName(Calendar.DAY_OF_WEEK,Calendar.LONG,Locale.getDefault());
            }
        }else{
            return calendar.getDisplayName(Calendar.DAY_OF_WEEK,Calendar.LONG,Locale.getDefault());
        }

    }
    /*

    public static Calendar stringToCalendar(String str, TimeZone tz) {
        Date date = null;
        Calendar calendar = Calendar.getInstance(tz);
        try {
            date = format.parse(str);
            calendar.setTime(date);
            return calendar;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;

    }

    public static Calendar longToCalendar(long time, TimeZone tz) {

        Calendar calendar = Calendar.getInstance(tz);

        calendar.setTimeInMillis(time);
        return calendar;

    }







    public static String getTimeInFormat1(Calendar cal1, Context con,
                                          boolean withTime) {
        SimpleDateFormat format2;
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(con);
        String time = calendarToString(cal1);
        if (withTime) {
            format2 = new SimpleDateFormat(sharedPrefs.getString(
                    Settings.P_FORMAT, ""));
        } else {
            format2 = new SimpleDateFormat(sharedPrefs.getString(
                    Settings.P_FORMAT, "").split(" ")[0]);
        }
        try {
            Date date = format.parse(time);
            format2.setTimeZone(TimeZone.getDefault());
            String formattedDate = format2.format(date);
            return formattedDate;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }*/
}
