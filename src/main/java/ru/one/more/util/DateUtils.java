package ru.one.more.util;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by aboba on 28.01.17.
 */
public class DateUtils {
    private static AtomicBoolean reload = new AtomicBoolean(true);
    private static final List<LocaleDateFormat> dfList = new ArrayList<>();
    private static WeakHashMap<String, SimpleDateFormat> cache = new WeakHashMap<>(10, 0.85f);

    public static void reloadRulesIfNeed() {
        if (reload.get()) {
            try {
                List<LocaleDateFormat> tempList = new ArrayList<>();
                String dateFormatsDef = IOUtils.toString(DateUtils.class.getClassLoader()
                        .getResourceAsStream("df.rules"));
                List<String> lines = Arrays.asList(dateFormatsDef.split("\n"));
                Locale currLocale = Locale.getDefault();

                for (String line : lines) {
                    if (line.startsWith("loc: ")) {
                        currLocale = new Locale(line.substring(line.indexOf(" ")));
                    } else {
                        tempList.add(new LocaleDateFormat(currLocale, line));
                    }
                }
                synchronized (dfList) {
                    dfList.clear();
                    dfList.addAll(tempList);
                }
                reload.set(false);
            } catch (IOException e) {
                reload.compareAndSet(false, true);
            }
        }

    }

    public static Optional<Date> tryToParseDate(String id, String str) {
        if (cache.get(id) != null) {
            try {
                return Optional.of(cache.get(id).parse(str));
            } catch (ParseException e) {
                cache.remove(id);
            }
        }
        List<LocaleDateFormat> dateFormats = new ArrayList<>();
        synchronized (dfList) {
            dateFormats.addAll(dfList);
        }
        for (LocaleDateFormat dateFormat : dateFormats)  {
            try {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat.df, dateFormat.locale);
                Date d = simpleDateFormat.parse(str);
                cache.put(id, simpleDateFormat);
                return Optional.of(d);
            } catch (ParseException ignored) {}
        }
        reload.set(true);
        return Optional.empty();
    }

    private static class LocaleDateFormat {
        final Locale locale;
        final String df;
        public LocaleDateFormat(Locale locale, String df) {
            this.locale = locale;
            this.df = df;
        }
    }
}
