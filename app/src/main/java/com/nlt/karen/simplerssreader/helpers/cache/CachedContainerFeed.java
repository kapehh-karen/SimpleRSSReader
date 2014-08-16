package com.nlt.karen.simplerssreader.helpers.cache;

import android.content.Context;

import com.nlt.karen.simplerssreader.core.rss.Message;
import com.nlt.karen.simplerssreader.helpers.db.NewsDB;

import java.util.Date;
import java.util.List;

/**
 * Created by Karen on 09.08.2014.
 */
public class CachedContainerFeed {
    private NewsDB newsDB;

    private List<Message> posts;
    private Date dateUpdate;
    private String urlRSS;
    private boolean isUpdated;

    public CachedContainerFeed(Context context) {
        this.newsDB = new NewsDB(context);
        this.posts = newsDB.getMessages();
        this.isUpdated = false;
    }

    public void setPosts(List<Message> messages) {
        posts = messages;

        // Записываем новую дату записи
        dateUpdate = new Date();

        // Добавляем записи в базу
        newsDB.addMessages(posts);

        isUpdated = true;
    }

    public List<Message> getPosts() {
        return posts;
    }

    public Date getDateUpdate() {
        return dateUpdate;
    }

    public boolean isUpdated() {
        return isUpdated;
    }

    public String getUrlRSS() {
        return urlRSS;
    }

    public void setUrlRSS(String urlRSS) {
        this.urlRSS = urlRSS;
    }

    public void closeDB() {
        newsDB.close();
    }
}
