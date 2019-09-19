package com.yadanar.carrentalservice.util;

import android.text.TextUtils;
import android.widget.EditText;

public class UiUtil {

    public static void setError(EditText edt) {
        edt.setError("*required");
    }

    public static String getText_(EditText edt) {
        return edt.getText().toString().trim();
    }

    public static double getNumber(EditText edt) {
        String s = edt.getText().toString().trim();
        if (TextUtils.isEmpty(s)) {
            return 0;
        }
        return Double.valueOf(s);
    }

    public static int getIntNumber(EditText edt) {
        return (int) getNumber(edt);
    }
}
