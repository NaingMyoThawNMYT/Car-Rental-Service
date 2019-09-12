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

    public static int getNumber(EditText edt) {
        String s = edt.getText().toString().trim();
        if (TextUtils.isEmpty(s)) {
            return 0;
        }
        return Integer.valueOf(s);
    }
}
