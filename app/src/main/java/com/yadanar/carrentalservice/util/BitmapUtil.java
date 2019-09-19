package com.yadanar.carrentalservice.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class BitmapUtil {

//    public static byte[] bitmapToByteArray(Bitmap bitmap) {
//        ByteArrayOutputStream stream = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
//        byte[] byteArray = stream.toByteArray();
//        bitmap.recycle();
//        return byteArray;
//    }

    public static Bitmap byteArrayToBitmap(byte[] bytes) {
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
}
