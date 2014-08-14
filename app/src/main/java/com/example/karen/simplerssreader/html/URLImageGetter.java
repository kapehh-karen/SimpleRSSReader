package com.example.karen.simplerssreader.html;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.text.Html;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.HashMap;

/**
 * Created by Karen on 13.08.2014.
 */
public class URLImageGetter implements Html.ImageGetter {
    public static HashMap<String, Drawable> listDrawableHashMap = new HashMap<String, Drawable>(); // TODO: Remove

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

        // TODO: Remove
        if (listDrawableHashMap.containsKey(source)) {
            return listDrawableHashMap.get(source);
        }

        URLBitmapDrawable urlBitmapDrawable = new URLBitmapDrawable();

        // get the actual source
        new ImageGetterAsyncTask(urlBitmapDrawable).execute(source);

        // return reference to URLDrawable where I will change with actual image from
        // the src tag
        return urlBitmapDrawable;
    }

    public class ImageGetterAsyncTask extends AsyncTask<String, Void, Drawable> {
        URLBitmapDrawable urlBitmapDrawable;

        public ImageGetterAsyncTask(URLBitmapDrawable d) {
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

            // set the correct bound according to the result from HTTP call
            urlBitmapDrawable.setBounds(0, 0, result.getIntrinsicWidth(), result.getIntrinsicHeight());

            // change the reference of the current drawable to the result
            // from the HTTP call
            urlBitmapDrawable.drawable = result;

            // redraw the image by invalidating the container
            //URLImageGetter.this.container.requestLayout();
            //URLImageGetter.this.container.invalidate();
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
                Drawable drawable = Drawable.createFromStream(is, "src");
                drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                URLImageGetter.listDrawableHashMap.put(urlString, drawable); // TODO: Remove
                return drawable;
            } catch (Exception e) {
                return null;
            }
        }

        private InputStream fetch(String urlString) throws MalformedURLException, IOException {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpGet request = new HttpGet(urlString);
            HttpResponse response = httpClient.execute(request);
            return response.getEntity().getContent();
        }
    }
}
