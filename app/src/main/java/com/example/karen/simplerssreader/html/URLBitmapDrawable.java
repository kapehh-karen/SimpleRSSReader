package com.example.karen.simplerssreader.html;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

/**
 * Created by Karen on 13.08.2014.
 */
public class URLBitmapDrawable extends BitmapDrawable {
    public Drawable drawable;

    public URLBitmapDrawable() {
        super();
    }

    public URLBitmapDrawable(Drawable drawable) {
        this();
        this.drawable = drawable;
        setBounds(drawable.getBounds());
    }

    @Override
    public void draw(Canvas canvas) {
        if(drawable != null) {
            drawable.draw(canvas);
        }
    }
}