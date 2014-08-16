package com.nlt.karen.simplerssreader.html;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.text.Html;
import android.widget.TextView;

import com.nlt.karen.simplerssreader.R;
import com.nlt.karen.simplerssreader.helpers.cache.CachedDrawable;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

/**
 * Created by Karen on 13.08.2014.
 */
public class URLImageGetter implements Html.ImageGetter {
    CachedDrawable cachedDrawable;
    Context context;
    TextView textView;

    public URLImageGetter(TextView textView, Context context, CachedDrawable cachedDrawable) {
        this.cachedDrawable = cachedDrawable;
        this.context = context;
        this.textView = textView;
    }

    public Drawable getDrawable(String source) {
        // root пути
        if (source.startsWith("//")) {
            source = "http:" + source;
        } else if (source.startsWith("/")) {
            source = "http:/" + source;
        }

        URLBitmapDrawable urlBitmapDrawable = new URLBitmapDrawable(
            context.getResources().getDrawable(R.drawable.image_loading)
        );

        try {
            if (cachedDrawable.inCache(source)) {
                new CacheImageGetterAsyncTask(urlBitmapDrawable).execute(source);
            } else {
                new InternetImageGetterAsyncTask(urlBitmapDrawable).execute(source);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return urlBitmapDrawable;
    }

    private class CacheImageGetterAsyncTask extends AsyncTask<String, Void, Drawable> {
        URLBitmapDrawable urlBitmapDrawable;

        public CacheImageGetterAsyncTask(URLBitmapDrawable urlBitmapDrawable) {
            this.urlBitmapDrawable = urlBitmapDrawable;
        }

        @Override
        protected Drawable doInBackground(String... params) {
            try {
                return cachedDrawable.load(params[0]);
            } catch (IOException e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(Drawable result) {
            if (result == null) {
                return;
            }
            urlBitmapDrawable.setDrawable(result);
            textView.setText(textView.getText());
        }
    }

    private class InternetImageGetterAsyncTask extends AsyncTask<String, Void, Drawable> {
        URLBitmapDrawable urlBitmapDrawable;

        public InternetImageGetterAsyncTask(URLBitmapDrawable d) {
            this.urlBitmapDrawable = d;
        }

        @Override
        protected Drawable doInBackground(String... params) {
            String source = params[0];
            return fetchDrawable(source);
        }

        @Override
        protected void onPostExecute(Drawable result) {
            if (result == null) {
                return;
            }
            urlBitmapDrawable.setDrawable(result);
            textView.setText(textView.getText());
        }

        public Drawable fetchDrawable(String urlString) {
            try {
                InputStream is = fetch(urlString);
                Drawable drawable = BitmapDrawable.createFromStream(is, null);
                cachedDrawable.save(urlString, drawable);
                return drawable;
            } catch (Exception e) {
                return null;
            }
        }

        private InputStream fetch(String urlString) throws IOException {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpGet request = new HttpGet(urlString);
            HttpResponse response = httpClient.execute(request);
            return response.getEntity().getContent();
        }
    }
}
