package ru.one.more.util;

import com.mashape.unirest.http.exceptions.UnirestException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Optional;

/**
 * Created by aboba on 28.01.17.
 */
public class StrUtils {
    
    public static boolean isWord(String s) {
        return s != null && !s.trim().isEmpty();
    }

    public static String getStackTraceText(Throwable e) {
        StackTraceElement[] trace = e.getStackTrace();
        StringBuilder b = new StringBuilder();
        for (StackTraceElement element : trace) {
            b.append(element.toString());
        }
        return b.toString();
    }

    public static Optional<Date> tryToParseDate(String str) {
        SimpleDateFormat sdf1 = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z", Locale.ENGLISH);
        SimpleDateFormat sdf2 = new SimpleDateFormat("d MMM yyyy HH:mm:ss Z", Locale.ENGLISH);
        try {
            return Optional.of(sdf1.parse(str));
        } catch (ParseException ignore) {
            try {
                return Optional.of(sdf2.parse(str));
            } catch (ParseException e) {
                return Optional.empty();
            }
        }
    }
}
