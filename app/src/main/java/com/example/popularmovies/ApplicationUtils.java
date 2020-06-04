package com.example.popularmovies;

import android.content.res.Resources;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

class ApplicationUtils {

    static String formattedDateWithLocal(String dateString) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        Date date;
        try {
            date = format.parse(dateString.replaceAll("Z$", "+0000"));
        } catch (ParseException e) {
            e.printStackTrace();
            //if we have an err just return the string we were given
            return dateString;
        }
        DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM, Resources.getSystem().getConfiguration().locale);
        assert date != null;
        return df.format(date);
    }
}