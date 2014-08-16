package com.nlt.karen.simplerssreader.helpers.cache;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Base64;

import com.nlt.karen.simplerssreader.core.cache.CacheManager;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

/**
 * Created by Karen on 14.08.2014.
 */
public class CachedDrawable {
    Context context;
    CacheManager cacheManager;

    public CachedDrawable(Context context) {
        this.context = context;
        this.cacheManager = new CacheManager(context, "img");
    }

    public boolean inCache(String url) throws UnsupportedEncodingException {
        String urlBase64 = getBase64(url);
        return cacheManager.getFile(urlBase64).exists();
    }

    public void save(String url, Drawable drawable) throws IOException {
        String urlBase64 = getBase64(url);
        Bitmap bitmap = drawableToBitmap(drawable);
        OutputStream stream = cacheManager.cacheDataStream(urlBase64);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        stream.close();
    }

    public Drawable load(String url) throws IOException {
        String urlBase64 = getBase64(url);
        InputStream stream = cacheManager.retrieveDataStream(urlBase64);
        if (stream == null) {
            return null;
        }
        Drawable drawable = BitmapDrawable.createFromStream(stream, null);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        return drawable;
    }

    private String getBase64(String fileName) throws UnsupportedEncodingException {
        return Base64.encodeToString(fileName.getBytes("UTF-8"),
               Base64.URL_SAFE | Base64.NO_WRAP);
    }

    private Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable)drawable).getBitmap();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }
}
