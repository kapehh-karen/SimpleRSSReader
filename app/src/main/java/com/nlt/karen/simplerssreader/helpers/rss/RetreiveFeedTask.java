package com.nlt.karen.simplerssreader.helpers.rss;

import java.util.List;

import android.os.AsyncTask;

import com.nlt.karen.simplerssreader.core.rss.Message;
import com.nlt.karen.simplerssreader.core.rss.XmlPullFeedParser;

public class RetreiveFeedTask extends AsyncTask<String, Void, List<Message>> {
    private IRetreiveFeedEvent retreiveFeedEvent;

    public void setRetreiveFeedEvent(IRetreiveFeedEvent context) {
        retreiveFeedEvent = context;
    }

    protected List<Message> doInBackground(String... urls) {
        try {
        	XmlPullFeedParser xmlRss = new XmlPullFeedParser(urls[0]);
            return xmlRss.parse();
        } catch (Exception e) {
            return null;
        }
    }

    protected void onPostExecute(List<Message> messages) {
        retreiveFeedEvent.onRetreiveFeed(messages);
    }
}