package com.example.karen.simplerssreader.html;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.text.Html;
import android.widget.TextView;

import com.example.karen.simplerssreader.Main;

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
    Context c;
    TextView container;

    /***
     * Construct the URLImageParser which will execute AsyncTask and refresh the container
     * @param t
     * @param c
     */
    public URLImageGetter(TextView t, Context c) {
        this.c = c;
        this.container = t;
    }

    public Drawable getDrawable(String source) {
        // root пути
        if (source.startsWith("//")) {
            source = "http:" + source;
        } else if (source.startsWith("/")) {
            source = "http:/" + source;
        }

        URLBitmapDrawable urlBitmapDrawable = new URLBitmapDrawable();

        try {
            if (Main.cachedDrawable.inCache(source)) {
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
                return Main.cachedDrawable.load(params[0]);
            } catch (IOException e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(Drawable result) {
            if (result == null) {
                return;
            }
            urlBitmapDrawable.setBounds(0, 0, result.getIntrinsicWidth(), result.getIntrinsicHeight());
            urlBitmapDrawable.drawable = result;
            URLImageGetter.this.container.setText(URLImageGetter.this.container.getText());
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

            urlBitmapDrawable.setBounds(0, 0, result.getIntrinsicWidth(), result.getIntrinsicHeight());
            urlBitmapDrawable.drawable = result;
            URLImageGetter.this.container.setText(URLImageGetter.this.container.getText());
        }

        /***
         * Get the Drawable from URL
         * @param urlString
         * @return
         */
        public Drawable fetchDrawable(String urlString) {
            try {
                InputStream is = fetch(urlString);
                Drawable drawable = BitmapDrawable.createFromStream(is, null);
                drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                Main.cachedDrawable.save(urlString, drawable);
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
