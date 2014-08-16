package com.nlt.karen.simplerssreader.helpers.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.provider.BaseColumns;

import com.nlt.karen.simplerssreader.core.rss.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Karen on 16.08.2014.
 */
public class NewsDB extends SQLiteOpenHelper implements BaseColumns {
    private static String TABLE_NAME = "news";
    private static String FIELD_TITLE = "title";
    private static String FIELD_LINK = "link";
    private static String FIELD_DESCRIPTION = "description";
    private static String FIELD_DATE = "date";

    public NewsDB(Context context) {
        super(context, "news.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
            "CREATE TABLE " + TABLE_NAME + " ("
                + NewsDB._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + FIELD_TITLE + " TEXT,"
                + FIELD_LINK + " TEXT,"
                + FIELD_DESCRIPTION + " TEXT,"
                + FIELD_DATE + " TEXT"
            + ");"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void addMessages(List<Message> messages) {
        SQLiteDatabase database = getWritableDatabase();
        database.delete(TABLE_NAME, null, null); // Очищаем перед добавлением

        String sql = String.format("INSERT INTO %s (%s, %s, %s, %s) VALUES (?, ?, ?, ?);",
                TABLE_NAME, FIELD_TITLE, FIELD_LINK, FIELD_DESCRIPTION, FIELD_DATE);
        SQLiteStatement statement = database.compileStatement(sql);
        database.beginTransaction();

        for (Message message : messages) {
            statement.clearBindings();
            statement.bindString(1, message.getTitle());
            statement.bindString(2, message.getUrl());
            statement.bindString(3, message.getDescription());
            statement.bindString(4, message.getDate());
            statement.execute();
        }

        database.setTransactionSuccessful();
        database.endTransaction();
        database.close();
    }

    public List<Message> getMessages() {
        List<Message> res = new ArrayList<Message>();
        SQLiteDatabase database = getReadableDatabase();

        Cursor cursor = database.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        while (cursor.moveToNext()) {
            Message message = new Message();
            message.setTitle(cursor.getString(1));
            message.setLink(cursor.getString(2));
            message.setDescription(cursor.getString(3));
            message.setDate(cursor.getString(4));
            res.add(message);
        }

        cursor.close();
        database.close();
        return res;
    }
}