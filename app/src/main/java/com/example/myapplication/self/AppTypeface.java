package com.example.myapplication.self;

import android.content.Context;
import android.graphics.Typeface;

import androidx.core.content.res.ResourcesCompat;

import com.example.myapplication.R;

public class AppTypeface {
//        Typeface face = Typeface.createFromAsset(context.getAssets(), "fonts/epimodem.ttf");

    public Typeface setGorditaBlack(Context context) {
        return ResourcesCompat.getFont(context, R.font.gordita_black);
    }

    public static  Typeface setGorditaBlackItalic(Context context) {
        return ResourcesCompat.getFont(context, R.font.gordita_black_italic);
    }

    public Typeface setGorditaBold(Context context) {
        return ResourcesCompat.getFont(context, R.font.gordita_bold);
    }

    public Typeface setGorditaBoldItalic(Context context) {
        return ResourcesCompat.getFont(context, R.font.gordita_bold_italic);
    }

    public Typeface setGorditaLight(Context context) {
        return ResourcesCompat.getFont(context, R.font.gordita_light);
    }

    public Typeface setGorditaItalic(Context context) {
        return ResourcesCompat.getFont(context, R.font.gordita_light_italic);
    }

    public Typeface setGorditaMedium(Context context) {
        return ResourcesCompat.getFont(context, R.font.gordita_medium);
    }

    public Typeface setGorditaMediumItalic(Context context) {
        return ResourcesCompat.getFont(context, R.font.gordita_medium_italic);
    }

    public Typeface setGorditaRegular(Context context) {
        return ResourcesCompat.getFont(context, R.font.gordita_regular);
    }

    public Typeface setGorditaRegularItalic(Context context) {
        return ResourcesCompat.getFont(context, R.font.gordita_regular_italic);
    }

    public Typeface setGorditaThin(Context context) {
        return ResourcesCompat.getFont(context, R.font.gordita_thin);
    }

    public Typeface setGorditaThinItalic(Context context) {
        return ResourcesCompat.getFont(context, R.font.gordita_thin_italic);
    }

    public Typeface setGorditaUltra(Context context) {
        return ResourcesCompat.getFont(context, R.font.gordita_ultra);
    }

    public Typeface setGorditaUltraItalic(Context context) {
        return ResourcesCompat.getFont(context, R.font.gordita_ultra_italic);
    }
}
