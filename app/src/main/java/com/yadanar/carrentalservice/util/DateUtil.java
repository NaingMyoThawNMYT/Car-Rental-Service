package com.yadanar.carrentalservice.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    public static String displayDateOnlyFormat(Date date) {
        return new SimpleDateFormat("dd/MM/yyyy").format(date);
    }

    public static String displayTimeOnlyFormat(Date date) {
        return new SimpleDateFormat("hh:mm a").format(date);
    }

    public static String displayDateAndTimeFormat(Date date) {
        return new SimpleDateFormat("dd/MM/yyyy hh:mm a").format(date);
    }

    public static double hoursDifference(Date date1, Date date2) {
        final double MILLI_TO_HOUR = 3.6e+6; // 1000 * 60 * 60
        return (date1.getTime() - date2.getTime()) / MILLI_TO_HOUR;
    }
}
