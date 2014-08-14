package com.example.karen.simplerssreader;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class Settings extends Activity {
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        editText = (EditText) findViewById(R.id.settingsRssUrl);
        editText.setText(Main.cachedContainerFeed.getUrlRSS());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    public void onClickOK(View view) {
        String strUrl = editText.getText().toString();
        if (strUrl.length() == 0) {
            Toast.makeText(this, R.string.error_need_url, Toast.LENGTH_LONG).show();
            return;
        }
        if (!strUrl.startsWith("http://")) {
            Toast.makeText(this, R.string.error_invalid_protocol, Toast.LENGTH_LONG).show();
            return;
        }
        if (!strUrl.matches("http://[a-zA-Z_0-9\\-]+(\\.\\w[a-zA-Z_0-9\\-]+)?(/[#&\\n\\-=?\\+\\%/\\.\\w]+)?")) {
            Toast.makeText(this, R.string.error_invalid_url, Toast.LENGTH_LONG).show();
            return;
        }
        Intent intent = new Intent();
        intent.putExtra("rss", strUrl);
        setResult(RESULT_OK, intent);
        finish();
    }
}
