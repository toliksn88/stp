package com.innowise.secret_santa.util;

import com.innowise.secret_santa.constants_message.Constants;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

public final class CalendarUtils {

    private CalendarUtils() {
    }

    public static String convertMilliSecondsToFormattedDate(Long milliSeconds) {

        SimpleDateFormat format = new SimpleDateFormat(Constants.DATE_FORMAT);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);

        return format.format(calendar.getTime());
    }

    public static LocalDateTime getFormatDate(LocalDateTime localDateTime) {

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(Constants.DATE_FORMAT);
        String data = dtf.format(localDateTime);
        return LocalDateTime.parse(data, dtf);
    }
}
