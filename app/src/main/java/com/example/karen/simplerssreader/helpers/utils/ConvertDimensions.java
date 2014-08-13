package com.example.karen.simplerssreader.helpers.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;

/**
 * Created by Karen on 13.08.2014.
 */
public class ConvertDimensions {
    private Resources r;

    public ConvertDimensions(Context context) {
        this.r = context.getResources();
    }

    public int fromDpToPixels(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
    }
}
