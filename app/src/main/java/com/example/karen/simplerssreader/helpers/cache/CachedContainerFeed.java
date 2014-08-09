package com.example.karen.simplerssreader.helpers.cache;

import com.example.karen.simplerssreader.core.rss.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Karen on 09.08.2014.
 */
public class CachedContainerFeed {
    private List<Message> posts = new ArrayList<Message>();

    public CachedContainerFeed() {
        // TODO: Грузим из кеша
    }

    public void setPosts(List<Message> messages) {
        posts = messages;
    }

    public List<Message> getPosts() {
        return posts;
    }
}
