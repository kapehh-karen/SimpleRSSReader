package com.example.karen.simplerssreader.core.rss;

import java.util.List;

import android.os.AsyncTask;

public class RetreiveFeedTask extends AsyncTask<String, Void, List<Message>> {
    protected List<Message> doInBackground(String... urls) {
        try {
        	XmlPullFeedParser xmlRss = new XmlPullFeedParser(urls[0]);
            return xmlRss.parse();
        } catch (Exception e) {
            return null;
        }
    }

    protected void onPostExecute(List<Message> messages) {
        if (messages == null) return;
        //MyApplication.instance.getCFactoryNews().AddListData(messages);
        // TODO
    }
}