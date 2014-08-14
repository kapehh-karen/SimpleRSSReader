package com.example.karen.simplerssreader.helpers.cache;

import android.content.Context;

import com.example.karen.simplerssreader.core.cache.CacheManager;
import com.example.karen.simplerssreader.core.rss.Message;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Karen on 09.08.2014.
 */
public class CachedContainerFeed {
    private CacheManager cacheManager;
    private List<Message> posts = new ArrayList<Message>();
    private Date dateUpdate;
    private String urlRSS;

    public CachedContainerFeed(Context context) {
        this.cacheManager = new CacheManager(context, "feed");
        // TODO: Грузим из кеша
    }

    public void setPosts(List<Message> messages) {
        posts = messages;

        // Записываем новую дату записи
        dateUpdate = new Date();

        // TODO: Асинхронно записываем в кеш
    }

    public List<Message> getPosts() {
        return posts;
    }

    public Date getDateUpdate() {
        return dateUpdate;
    }

    public String getUrlRSS() {
        return urlRSS;
    }

    public void setUrlRSS(String urlRSS) {
        this.urlRSS = urlRSS;
    }
}
