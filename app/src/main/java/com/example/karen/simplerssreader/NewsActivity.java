package com.example.karen.simplerssreader;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.karen.simplerssreader.core.rss.Message;
import com.example.karen.simplerssreader.helpers.adapters.NewsListAdapter;
import com.example.karen.simplerssreader.helpers.rss.IRetreiveFeedEvent;
import com.example.karen.simplerssreader.helpers.rss.RetreiveFeedTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class NewsActivity extends Activity implements SwipeRefreshLayout.OnRefreshListener, IRetreiveFeedEvent, AdapterView.OnItemClickListener {

    // Формат даты последнего обновления списка
    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss");

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
        listView.setOnItemClickListener(this);

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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Message message = (Message) arrayList.get(position);
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(message.getUrl()));
        startActivity(browserIntent);
    }

    @Override
    public void onRefresh() {
        if (retreiveFeedTask == null) {
            retreiveFeedTask = new RetreiveFeedTask();
            retreiveFeedTask.setRetreiveFeedEvent(this);
            retreiveFeedTask.execute("http://habrahabr.ru/rss/hubs/?with_hubs=true&with_tags=true" /*"http://bash.im/rss/"*/);
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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            Date dateUpdate = Main.cachedContainerFeed.getDateUpdate();
            if (dateUpdate != null) {
                getActionBar().setSubtitle(simpleDateFormat.format(dateUpdate));
            }
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
            startActivity(new Intent(this, Settings.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
