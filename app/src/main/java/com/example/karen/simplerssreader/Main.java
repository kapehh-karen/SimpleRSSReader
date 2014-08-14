package com.example.karen.simplerssreader;

import android.content.Context;

import com.example.karen.simplerssreader.helpers.cache.CachedBitmap;
import com.example.karen.simplerssreader.helpers.cache.CachedContainerFeed;
import com.example.karen.simplerssreader.helpers.utils.ApplicationConfig;

/**
 * Created by Karen on 09.08.2014.
 */
public class Main {
    public static CachedContainerFeed cachedContainerFeed;
    public static ApplicationConfig applicationConfig;
    public static CachedBitmap cachedBitmap;

    private static boolean initialized = false;

    public static void init(Context context) {
        if (initialized) {
            return;
        }
        cachedContainerFeed = new CachedContainerFeed(context);
        applicationConfig = new ApplicationConfig(context);
        cachedBitmap = new CachedBitmap(context);
        initialized = true;
    }

    public static boolean isInit() {
        return initialized;
    }
}
