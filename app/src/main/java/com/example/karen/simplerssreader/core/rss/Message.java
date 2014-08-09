package com.example.karen.simplerssreader.core.rss;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.Locale;
import java.text.SimpleDateFormat;

import java.text.ParseException;

public class Message implements Comparable<Message>{
    static SimpleDateFormat FORMATTER = 
        new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH);
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
    
    @Override
    public String toString() {
		return "toString()";
    }

    @Override
    public int hashCode() {
		return 0;
    }
    
    @Override
    public boolean equals(Object obj) {
		return false;
    }

    public int compareTo(Message another) {
        if (another == null) return 1;
        return another.date.compareTo(date);
    }
}