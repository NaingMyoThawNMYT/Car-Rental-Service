package com.yadanar.carrentalservice.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkUtil {

    public static boolean hasConnection(Context context) {
        ConnectivityManager conMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }
}
