package com.yadanar.carrentalservice.util;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

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

    public static void checkConnectionAndFinishActivity(Context context) {
        if (!NetworkUtil.hasConnection(context)) {
            Toast.makeText(context, "There is no internet connection.", Toast.LENGTH_SHORT).show();
            ((Activity) context).finish();
        }
    }
}
