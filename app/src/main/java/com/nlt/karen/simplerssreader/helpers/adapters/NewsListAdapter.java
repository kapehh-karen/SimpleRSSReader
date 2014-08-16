package com.nlt.karen.simplerssreader.helpers.adapters;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.nlt.karen.simplerssreader.R;
import com.nlt.karen.simplerssreader.core.rss.Message;
import com.nlt.karen.simplerssreader.helpers.cache.CachedDrawable;
import com.nlt.karen.simplerssreader.html.URLImageGetter;

import java.util.List;

/**
 * Created by Karen on 11.08.2014.
 */
public class NewsListAdapter extends ArrayAdapter<Object> {
    private static class ViewHolder {
        public TextView textViewTitle;
        public TextView textViewDescription;
    }

    CachedDrawable cachedDrawable;
    Context context;
    LayoutInflater layoutInflater;
    List<Object> objectList;

    public NewsListAdapter(Context context, CachedDrawable cachedDrawable, List<Object> objects) {
        super(context, R.layout.news_item_layout, objects);
        this.cachedDrawable = cachedDrawable;
        this.context = context;
        this.objectList = objects;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        View rowView = convertView;

        if (rowView == null) {
            rowView = layoutInflater.inflate(R.layout.news_item_layout, null, true);
            holder = new ViewHolder();
            holder.textViewTitle = (TextView) rowView.findViewById(R.id.newsItemTitle);
            holder.textViewDescription = (TextView) rowView.findViewById(R.id.newsItemDescription);
            rowView.setTag(holder);
        } else {
            holder = (ViewHolder) rowView.getTag();
        }

        processRow(holder, (Message) objectList.get(position));
        return rowView;
    }

    private void processRow(ViewHolder viewHolder, Message message) {
        viewHolder.textViewTitle.setText(message.getTitle());

        URLImageGetter urlImageGetter;
        if (viewHolder.textViewDescription.getTag() == null) {
            urlImageGetter = new URLImageGetter(viewHolder.textViewDescription, context, cachedDrawable);
            viewHolder.textViewDescription.setTag(urlImageGetter);
        } else {
            urlImageGetter = (URLImageGetter) viewHolder.textViewDescription.getTag();
        }
        viewHolder.textViewDescription.setText(Html.fromHtml(message.getDescription(), urlImageGetter, null));
    }
}
