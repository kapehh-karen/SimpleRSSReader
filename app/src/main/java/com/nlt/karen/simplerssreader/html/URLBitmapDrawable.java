package com.nlt.karen.simplerssreader.html;

import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

/**
 * Created by Karen on 13.08.2014.
 */
public class URLBitmapDrawable extends BitmapDrawable {
    private Drawable drawable;

    public URLBitmapDrawable() {
        super();
    }

    public URLBitmapDrawable(Drawable drawable) {
        this();
        setDrawable(drawable);
    }

    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
        this.drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        setBounds(this.drawable.getBounds());
    }

    @Override
    public void draw(Canvas canvas) {
        if(drawable != null) {
            drawable.draw(canvas);
        }
    }
}