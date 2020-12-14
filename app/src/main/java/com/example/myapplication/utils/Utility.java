package com.example.myapplication.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.example.myapplication.R;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;

public class Utility {
    private static ProgressDialog progress;
    private final static int COMPRESSED_RATIO = 13;
    private final static int perPixelDataSize = 4;

    public static boolean isConnectedToInternet(Context _context) {
        ConnectivityManager connectivity = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (NetworkInfo networkInfo : info) {
                    if (networkInfo.getType() != ConnectivityManager.TYPE_MOBILE_SUPL) {
                        if (networkInfo.getState() == NetworkInfo.State.CONNECTED) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public static String decompress(byte[] compressed) throws IOException {
        final int BUFFER_SIZE = 32;
        ByteArrayInputStream is = new ByteArrayInputStream(compressed);
        GZIPInputStream gis = new GZIPInputStream(is, BUFFER_SIZE);
        StringBuilder string = new StringBuilder();
        byte[] data = new byte[BUFFER_SIZE];
        int bytesRead;
        while ((bytesRead = gis.read(data)) != -1) {
            string.append(new String(data, 0, bytesRead));
        }
        gis.close();
        is.close();
        return string.toString();
    }


    public static void showLoadingDialog(Context context, String title, String content) {
        try {
            if (progress == null) {
                progress = ProgressDialog.show(context, title != null ? title : "", (content != null && content.length() > 0) ? content : context.getString(R.string.PLEASE_WAIT_MSG));
                progress.show();
            } else {
                if (progress.isShowing()) {
                    progress.dismiss();
                    progress = ProgressDialog.show(context, title != null ? title : "", (content != null && content.length() > 0) ? content : context.getString(R.string.PLEASE_WAIT_MSG));
                    progress.show();
                } else {
                    progress = ProgressDialog.show(context, title != null ? title : "", (content != null && content.length() > 0) ? content : context.getString(R.string.PLEASE_WAIT_MSG));
                    progress.show();
                }
            }
        } catch (Exception | Error e) {
            e.printStackTrace();
        }
    }

    public static void dismissLoadingDialog() {
        try {
            if (progress != null && progress.isShowing()) {
                progress.dismiss();
            }
        } catch (Exception | Error e) {
            e.printStackTrace();
        }
    }

    public static void toCallToast(String msg, Context context) {
        try {
            if (context != null) {
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception | Error e) {
            e.printStackTrace();
        }
    }

    public static byte[] getJPGLessThanMaxSize(Bitmap image, int maxSize) {
        int maxPixelCount = maxSize * 1024 * COMPRESSED_RATIO / perPixelDataSize;
        int imagePixelCount = image.getWidth() * image.getHeight();
        Bitmap reducedBitmap;
        if (imagePixelCount > maxPixelCount)
            reducedBitmap = getResizedBitmapLessThanMaxSize(image, maxSize);
        else reducedBitmap = image;

        float compressedRatio = 1;
        byte[] resultBitmap;
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        int jpgQuality = 70;
        do {
            reducedBitmap.compress(Bitmap.CompressFormat.JPEG, jpgQuality, outStream);
            resultBitmap = outStream.toByteArray();
            compressedRatio = resultBitmap.length / (reducedBitmap.getWidth() * reducedBitmap.getHeight() * perPixelDataSize);
            if (compressedRatio > (COMPRESSED_RATIO - 1)) {
                jpgQuality -= 1;
            } else if (compressedRatio > (COMPRESSED_RATIO * 0.8)) {
                jpgQuality -= 5;
            } else {
                jpgQuality -= 10;
            }
        } while (resultBitmap.length > (maxSize * 1024));
        return resultBitmap;
    }

    private static Bitmap getResizedBitmapLessThanMaxSize(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();
        float bitmapRatio = (float) width / (float) height;
        height = (int) Math.sqrt(maxSize * 1024 * COMPRESSED_RATIO / perPixelDataSize / bitmapRatio);
        width = (int) (height * bitmapRatio);
        return Bitmap.createScaledBitmap(image, width, height, true);
    }
}
