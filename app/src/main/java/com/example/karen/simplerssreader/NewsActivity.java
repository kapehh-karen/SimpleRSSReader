package com.example.karen.simplerssreader;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.karen.simplerssreader.core.rss.Message;
import com.example.karen.simplerssreader.helpers.adapters.NewsListAdapter;
import com.example.karen.simplerssreader.helpers.rss.IRetreiveFeedEvent;
import com.example.karen.simplerssreader.helpers.rss.RetreiveFeedTask;

import java.util.ArrayList;
import java.util.List;


public class NewsActivity extends Activity implements SwipeRefreshLayout.OnRefreshListener, IRetreiveFeedEvent {

    // Статичное поле, необходимо из-за пересоздания активити при повороте экрана
    private static RetreiveFeedTask retreiveFeedTask = null;

    private ListView listView;
    private TextView textView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ArrayAdapter arrayAdapter;
    private ArrayList<Object> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeColors(
            getResources().getColor(android.R.color.holo_orange_dark),
            getResources().getColor(android.R.color.holo_blue_light),
            getResources().getColor(android.R.color.holo_green_light),
            getResources().getColor(android.R.color.holo_red_light)
        );

        textView = (TextView) findViewById(R.id.newsTextView);

        arrayList = new ArrayList<Object>();
        arrayAdapter = new NewsListAdapter(this, arrayList);

        listView = (ListView) findViewById(R.id.newsListView);
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
        // Очищаем список
        arrayList.clear();

        // Получаем новости (из кеша, или уже загруженные ранее)
        List<Message> messages = Main.cachedContainerFeed.getPosts();
        for (Message message : messages) {
            arrayList.add(message);
        }

        // Говорим адаптеру чтоб обновил данные
        arrayAdapter.notifyDataSetChanged();

        // Если уж совсем ничего нет, отображаем соответствующую надпись
        if (arrayList.size() > 0) {
            textView.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
        } else {
            textView.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onRetreiveFeed(List<Message> messages) {
        retreiveFeedTask = null;
        mSwipeRefreshLayout.setRefreshing(false);
        if (messages != null) {
            Main.cachedContainerFeed.setPosts(messages);
            updateList();
            Toast.makeText(this, R.string.complete_loading, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, R.string.error_loading, Toast.LENGTH_SHORT).show();
        }
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
