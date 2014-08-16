package com.nlt.karen.simplerssreader;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nlt.karen.simplerssreader.core.rss.Message;
import com.nlt.karen.simplerssreader.helpers.adapters.NewsListAdapter;
import com.nlt.karen.simplerssreader.helpers.rss.IRetreiveFeedEvent;
import com.nlt.karen.simplerssreader.helpers.rss.RetreiveFeedTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


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

        Main.init(getApplicationContext());

        // Загружаем настройки
        Main.cachedContainerFeed.setUrlRSS(Main.applicationConfig.getSettings().getString("rss", ""));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            String updateTime = Main.applicationConfig.getSettings().getString("update", "");
            if (updateTime.length() > 0) {
                getActionBar().setSubtitle(updateTime);
            }
        }

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
        arrayAdapter = new NewsListAdapter(this, Main.cachedDrawable, arrayList);

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
    protected void onStop() {
        super.onStop();

        // Сохраняем настройки
        SharedPreferences.Editor editor = Main.applicationConfig.getSettings().edit();
        editor.putString("rss", Main.cachedContainerFeed.getUrlRSS());
        if (Main.cachedContainerFeed.isUpdated()) {
            editor.putString("update", dateToString(Main.cachedContainerFeed.getDateUpdate()));
        }
        editor.apply();

        // Закрываем базу
        Main.cachedContainerFeed.closeDB();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Message message = (Message) arrayList.get(position);
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(message.getUrl()));
        startActivity(browserIntent);
    }

    @Override
    public void onRefresh() {
        String urlRSS = Main.cachedContainerFeed.getUrlRSS();
        if (urlRSS == null || urlRSS.length() == 0) {
            Toast.makeText(this, R.string.error_noset_rss, Toast.LENGTH_SHORT).show();
            startSettings();
        } else {
            loadRSS();
        }
    }

    private String dateToString(Date date) {
        if (date == null) {
            return "";
        }
        return simpleDateFormat.format(date);
    }

    private void loadRSS() {
        if (retreiveFeedTask == null) {
            // Включаем анимацию загрузки
            mSwipeRefreshLayout.setRefreshing(true);

            retreiveFeedTask = new RetreiveFeedTask();
            retreiveFeedTask.setRetreiveFeedEvent(this);

            // "http://habrahabr.ru/rss/hubs/?with_hubs=true&with_tags=true"
            // "http://bash.im/rss/"
            retreiveFeedTask.execute(Main.cachedContainerFeed.getUrlRSS());
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

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                getActionBar().setSubtitle(dateToString(Main.cachedContainerFeed.getDateUpdate()));
            }

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
            startSettings();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void startSettings() {
        startActivityForResult(new Intent(this, Settings.class), 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && data != null) {
            Main.cachedContainerFeed.setUrlRSS(data.getStringExtra("rss"));
            loadRSS();
        }
    }
}
