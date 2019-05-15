package com.nikvay.business_application.utils;

import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

/**
 * Created by Tamboli on 14-Mar-17.
 */

public class FilesUtil {

    public static String ExternalPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Servicom/";

    public static String getStringImage(Bitmap bmp) {
        String encodedImage = null;
        if (bmp != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] imageBytes = baos.toByteArray();
            encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        }
        return encodedImage;
    }

}
