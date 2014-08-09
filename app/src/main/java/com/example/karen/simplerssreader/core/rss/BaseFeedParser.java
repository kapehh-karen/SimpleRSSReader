package com.example.karen.simplerssreader.core.rss;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public abstract class BaseFeedParser {
    static final String PUB_DATE = "pubDate";
    static final String DESCRIPTION = "description";
    static final String LINK = "link";
    static final String TITLE = "title";
    static final String ITEM = "item";
    static final String CHANNEL = "channel";
    static final String RSS = "rss";

    private URL feedUrl;

    protected BaseFeedParser(String feedUrl) {
        try {
            this.feedUrl = new URL(feedUrl);
        } catch (MalformedURLException e) {
            this.feedUrl = null;
        }
    }

    protected InputStream getInputStream() {
        try {
            return feedUrl.openConnection().getInputStream();
        } catch (IOException e) {
        	return null;
        }
    }

    public abstract List<Message> parse() throws Exception;
}