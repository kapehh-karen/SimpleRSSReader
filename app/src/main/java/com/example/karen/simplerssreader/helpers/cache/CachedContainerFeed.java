package com.example.karen.simplerssreader.helpers.cache;

import com.example.karen.simplerssreader.core.rss.Message;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Karen on 09.08.2014.
 */
public class CachedContainerFeed {
    private List<Message> posts = new ArrayList<Message>();
    private Date dateUpdate;

    public CachedContainerFeed() {
        // TODO: Грузим из кеша
    }

    public void setPosts(List<Message> messages) {
        dateUpdate = new Date();
        posts = messages;
    }

    public List<Message> getPosts() {
        return posts;
    }

    public Date getDateUpdate() {
        return dateUpdate;
    }
}
