package com.yadanar.carrentalservice.util;

import java.text.DecimalFormat;

public class NumberUtil {

    public static double getOneDigit(double value) {
        return Double.valueOf(new DecimalFormat("#.#").format(Math.round(value * 2) / 2d));
    }
}
