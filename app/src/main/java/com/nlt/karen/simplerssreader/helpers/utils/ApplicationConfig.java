package com.nlt.karen.simplerssreader.helpers.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Karen on 14.08.2014.
 */
public class ApplicationConfig {
    private static final String APP_PREFERENCES = "mysettings";

    private SharedPreferences mSettings;

    public ApplicationConfig(Context context) {
        mSettings = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
    }

    public SharedPreferences getSettings() {
        return mSettings;
    }
}
