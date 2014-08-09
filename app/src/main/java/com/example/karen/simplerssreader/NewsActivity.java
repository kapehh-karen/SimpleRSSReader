package com.example.karen.simplerssreader;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.karen.simplerssreader.core.rss.Message;
import com.example.karen.simplerssreader.helpers.rss.IRetreiveFeedEvent;
import com.example.karen.simplerssreader.helpers.rss.RetreiveFeedTask;

import java.util.ArrayList;
import java.util.List;


public class NewsActivity extends Activity implements SwipeRefreshLayout.OnRefreshListener, IRetreiveFeedEvent {

    // Статичное поле, необходимо из-за пересоздания активити при повороте экрана
    private static RetreiveFeedTask retreiveFeedTask = null;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ArrayAdapter arrayAdapter;
    private ArrayList<String> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeColors(
            getResources().getColor(android.R.color.holo_blue_bright),
            getResources().getColor(android.R.color.holo_green_light),
            getResources().getColor(android.R.color.holo_orange_light),
            getResources().getColor(android.R.color.holo_red_light)
        );

        arrayList = new ArrayList<String>();
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayList);

        ListView listView = (ListView) findViewById(android.R.id.list);
        listView.setAdapter(arrayAdapter);

        if (retreiveFeedTask != null) {
            /*
             Нужно для того, чтобы при повороте экрана, когда пересоздается активити,
             загрузка данных продолжалась корректно, а не пропадал весь прогресс.
              */
            mSwipeRefreshLayout.setRefreshing(true);
            retreiveFeedTask.setRetreiveFeedEvent(this);
        }

        updateList();
    }

    @Override
    public void onRefresh() {
        if (retreiveFeedTask == null) {
            retreiveFeedTask = new RetreiveFeedTask();
            retreiveFeedTask.setRetreiveFeedEvent(this);
            retreiveFeedTask.execute("http://habrahabr.ru/rss/hubs/?with_hubs=true&with_tags=true");
        }
    }

    private void updateList() {
        arrayList.clear();
        List<Message> messages = Main.cachedContainerFeed.getPosts();
        for (Message message : messages) {
            arrayList.add(message.getTitle());
        }
        arrayAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRetreiveFeed(List<Message> messages) {
        retreiveFeedTask = null;
        mSwipeRefreshLayout.setRefreshing(false);
        Main.cachedContainerFeed.setPosts(messages);
        updateList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.news, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
