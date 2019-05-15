package com.nikvay.business_application.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.provider.MediaStore;

/**
 * Created by cts on 12/4/17.
 */

public class CameraGalleryUtil {

    public static String ExternalPath = FilesUtil.ExternalPath; // Set your folder location
    private static final String TAG = CameraGalleryUtil.class.getSimpleName();
    private static int RESULT_LOAD = 1, RESULT_LOAD_IMAGE = 2;
    private static int SELECT_VIDEO_DIALOG = 5;

    public static void galleryImage(Context context) {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        ((Activity)context).startActivityForResult(galleryIntent, RESULT_LOAD);
    }

}
