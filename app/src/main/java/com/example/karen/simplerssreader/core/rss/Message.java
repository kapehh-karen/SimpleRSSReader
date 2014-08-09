package com.example.karen.simplerssreader.core.rss;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.text.SimpleDateFormat;

import java.text.ParseException;
import java.util.Map;

public class Message /*implements Comparable<Message>*/ {
    private static SimpleDateFormat messageDateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH);

    private Map<String, String> tagList = new HashMap<String, String>();

    public Date getDateFormat(String tagName) {

        return messageDateFormat.format(date);
    }

    public String getString(String tagName) {
        if (!tagList.containsKey(tagName)) {
            return null;
        } else {
            return tagList.get(tagName);
        }
    }

    public void setString(String tagName, String tagValue) {
        tagList.remove(tagName);
        tagList.put(tagName, tagValue);
    }

    /*
    private String title;
    private URL link;
    private String description;
    private Date date;

    public String getUrl() {
    	return link.toString();
    }

    public void setLink(String link) {
        try {
            this.link = new URL(link);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    public String getDate() {
        return FORMATTER.format(this.date);
    }

    public void setDate(String date) {
        while (!date.endsWith("00")){
            date += "0";
        }
        try {
			this.date = FORMATTER.parse(date.trim());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public String getTitle() {
    	return title;
    }

    public void setTitle(String title) {
    	this.title = title;
    }
    
    public String getDescription() {
    	return description;
    }

    public void setDescription(String description) {
    	this.description = description;
    }

    public int compareTo(Message another) {
        if (another == null) return 1;
        return another.date.compareTo(date);
    }*/
}