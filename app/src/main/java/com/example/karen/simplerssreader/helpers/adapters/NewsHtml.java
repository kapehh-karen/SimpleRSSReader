package com.example.karen.simplerssreader.helpers.adapters;

import android.text.Editable;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.text.style.URLSpan;
import android.util.Log;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

/**
 * Created by Karen on 12.08.2014.
 */
public class NewsHtml {

    public static Spanned fromHtml(String html) {
        SpannableString spanned = new SpannableString(Html.fromHtml(html));
        /*for (Object o : spanned.getSpans(0, spanned.length(), Object.class)) {
            Log.d("tag", o.toString());
            if (o instanceof URLSpan) {
                URLSpan urlSpan = (URLSpan) o;

            }
        }*/
        return spanned;
    }
}
