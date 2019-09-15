package com.yadanar.carrentalservice.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    public static String displayDateFormat(Date date) {
        return new SimpleDateFormat("dd/MM/yyyy").format(date);
    }

    public static String displayTimeFormat(Date date) {
        return new SimpleDateFormat("hh:mm a").format(date);
    }
}
