package com.example.karen.simplerssreader.helpers.cache;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Base64;
import android.util.Log;

import com.example.karen.simplerssreader.core.cache.CacheManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

/**
 * Created by Karen on 14.08.2014.
 */
public class CachedBitmap {
    CacheManager cacheManager;

    public CachedBitmap(Context context) {
        this.cacheManager = new CacheManager(context, "img");
    }

    public void save(String url, Drawable drawable) throws IOException {
        String urlBase64 = getBase64(url);
        cacheManager.cacheData("QWEQWE".getBytes(), urlBase64);
        // TODO: Write Object
    }

    public void save(String url, Bitmap bitmap) throws IOException {
        String urlBase64 = getBase64(url);
        cacheManager.cacheData("QWEQWE".getBytes(), urlBase64);
        // TODO: Write Object
    }

    public Bitmap load(String url) throws IOException {
        String urlBase64 = getBase64(url);
        byte[] data = cacheManager.retrieveData(urlBase64);
        if (data == null) {
            return null;
        }
        // TODO: Return Object
        return null;
    }

    private String getBase64(String fileName) throws UnsupportedEncodingException {
        return Base64.encodeToString(fileName.getBytes("UTF-8"), Base64.URL_SAFE | Base64.NO_WRAP);
    }
}
