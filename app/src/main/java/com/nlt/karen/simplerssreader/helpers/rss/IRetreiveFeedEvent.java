package com.nlt.karen.simplerssreader.helpers.rss;

import com.nlt.karen.simplerssreader.core.rss.Message;

import java.util.List;

/**
 * Created by Karen on 09.08.2014.
 */
public interface IRetreiveFeedEvent {
    public void onRetreiveFeed(List<Message> messages);
}
