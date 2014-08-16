package com.nlt.karen.simplerssreader.core.rss;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.Locale;
import java.text.SimpleDateFormat;

import java.text.ParseException;

public class Message {
    public static SimpleDateFormat FORMATTER = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH);

    /*private Map<String, String> tagMap = new HashMap<String, String>();
    private Map<String, List<String>> tagMapList = new HashMap<String, List<String>>();

    public Date getDate(String tagName) throws ParseException {
        return FORMATTER.parse(getString(tagName));
    }

    public Date getDate(String tagName, DateFormat dateFormat) throws ParseException {
        return dateFormat.parse(getString(tagName));
    }

    public void setDate(String tagName, Date date) {
        setString(tagName, FORMATTER.format(date));
    }

    public void setDate(String tagName, Date date, DateFormat dateFormat) {
        setString(tagName, dateFormat.format(date));
    }

    public String getString(String tagName) {
        if (!tagMap.containsKey(tagName)) {
            return null;
        } else {
            return tagMap.get(tagName);
        }
    }

    public void setString(String tagName, String tagValue) {
        tagMap.remove(tagName);
        tagMap.put(tagName, tagValue);
    }

    public void removeTag(String tagName) {
        tagMap.remove(tagName);
    }


    public void addStringArray(String tagName, String tagValue) {
        if (!tagMapList.containsKey(tagName)) {
            List<String> stringList = new ArrayList<String>();
            stringList.add(tagValue);
            tagMapList.put(tagName, stringList);
        } else {
            tagMapList.get(tagName).add(tagValue);
        }
    }*/

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
            this.link = null;
        }
    }

    public String getDate() {
        return FORMATTER.format(this.date);
    }

    public void setDate(String date) {
        try {
			this.date = FORMATTER.parse(date.trim());
        } catch (ParseException e) {
            this.date = null;
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
        return "Message{" +
                "title='" + title + '\'' +
                ", link=" + link +
                ", description='" + description + '\'' +
                ", date=" + date +
                '}';
    }
}