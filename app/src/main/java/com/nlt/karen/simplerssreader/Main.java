package com.nlt.karen.simplerssreader;

import android.content.Context;

import com.nlt.karen.simplerssreader.helpers.cache.CachedDrawable;
import com.nlt.karen.simplerssreader.helpers.cache.CachedContainerFeed;
import com.nlt.karen.simplerssreader.helpers.utils.ApplicationConfig;

/**
 * Created by Karen on 09.08.2014.
 */
public class Main {
    public static CachedContainerFeed cachedContainerFeed;
    public static ApplicationConfig applicationConfig;
    public static CachedDrawable cachedDrawable;

    private static boolean initialized = false;

    public static void init(Context context) {
        if (initialized) {
            return;
        }
        cachedContainerFeed = new CachedContainerFeed(context);
        applicationConfig = new ApplicationConfig(context);
        cachedDrawable = new CachedDrawable(context);
        initialized = true;
    }

    public static boolean isInit() {
        return initialized;
    }
}
