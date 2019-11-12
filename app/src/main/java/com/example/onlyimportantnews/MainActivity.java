package com.example.onlyimportantnews;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.health.HealthStats;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ListView;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private static final String SETTINGS_DATA = "settingsData";
    private static final String IMPORTANCE_LEVEL = "importanceLevel";
    private static final String WORLD_NEWS_BOOLEAN = "worldNewsBoolean";
    private static final String US_POLITICS_BOOLEAN = "usPoliticsBoolean";
    private static final String UK_POLITICS_BOOLEAN = "ukPoliticsBoolean";
    private static final String TECHNOLOGY_BOOLEAN = "technologyBoolean";
    private static final String SCIENCE_BOOLEAN = "scienceBoolean";
    private static final String SPACE_BOOLEAN = "spaceBoolean";
    private static final String MOVIES_AND_TV_BOOLEAN = "moviesAndTvBoolean";
    private static final String ECONOMY_BOOLEAN = "economyBoolean";
    private static final String SPORTS_BOOLEAN = "sportsBoolean";
    private static final String HEALTH_BOOLEAN = "healthBoolean";

    private int storiesToGet;
    private ArrayList<String> newsSourceUrls = new ArrayList<>();

    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        swipeRefreshLayout = findViewById(R.id.swipeRefresh);

        swipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        download();
                    }
                }
        );

        download();
    }

    private ArrayList<String> getSelectedNewsSources(int numberOfStoriesToDownload) {
        SharedPreferences settings = getSharedPreferences(SETTINGS_DATA, MODE_PRIVATE);

        newsSourceUrls.clear(); // Clean so we don't duplicate news sources

        if (settings.getBoolean(WORLD_NEWS_BOOLEAN, true)) {
            newsSourceUrls.add("https://www.reddit.com/r/worldnews/top.json?t=month&limit=" + numberOfStoriesToDownload);
        }

        if (settings.getBoolean(US_POLITICS_BOOLEAN, true)) {
            newsSourceUrls.add("https://www.reddit.com/r/politics/top.json?t=month&limit=" + numberOfStoriesToDownload);
        }

        if (settings.getBoolean(UK_POLITICS_BOOLEAN, true)) {
            newsSourceUrls.add("https://www.reddit.com/r/ukpolitics/top.json?t=month&limit=" + numberOfStoriesToDownload);
        }

        if (settings.getBoolean(TECHNOLOGY_BOOLEAN, true)) {
            newsSourceUrls.add("https://www.reddit.com/r/tech/top.json?t=month&limit=" + numberOfStoriesToDownload);
        }

        if (settings.getBoolean(SCIENCE_BOOLEAN, true)) {
            newsSourceUrls.add("https://www.reddit.com/r/science/top.json?t=month&limit=" + numberOfStoriesToDownload);
        }

        if (settings.getBoolean(SPACE_BOOLEAN, true)) {
            newsSourceUrls.add("https://www.reddit.com/r/space/top.json?t=month&limit=" + numberOfStoriesToDownload);
        }

        if (settings.getBoolean(MOVIES_AND_TV_BOOLEAN, true)) {
            newsSourceUrls.add("https://www.reddit.com/r/movies/top.json?t=month&limit=" + numberOfStoriesToDownload);
            newsSourceUrls.add("https://www.reddit.com/r/television/top.json?t=month&limit=" + numberOfStoriesToDownload);
        }

        if (settings.getBoolean(ECONOMY_BOOLEAN, true)) {
            newsSourceUrls.add("https://www.reddit.com/r/economics/top.json?t=month&limit=" + numberOfStoriesToDownload);
        }

        if (settings.getBoolean(SPORTS_BOOLEAN, true)) {
            newsSourceUrls.add("https://www.reddit.com/r/sports/top.json?t=month&limit=" + numberOfStoriesToDownload);
        }

        if (settings.getBoolean(HEALTH_BOOLEAN, true)) {
            newsSourceUrls.add("https://www.reddit.com/r/health/top.json?t=month&limit=" + numberOfStoriesToDownload);
        }

        return newsSourceUrls;
    }

    private int getNumberOfStoriesToDownload() {
        SharedPreferences settings = getSharedPreferences(SETTINGS_DATA, MODE_PRIVATE);

        switch (settings.getInt(IMPORTANCE_LEVEL, 1)) {
            case 0:
                storiesToGet = 20;
                break;
            case 1:
                storiesToGet = 10;
                break;
            case 2:
                storiesToGet = 3;
                break;
        }

        return storiesToGet;
    }

    private void download() {
        storiesToGet = getNumberOfStoriesToDownload();
        newsSourceUrls = getSelectedNewsSources(storiesToGet);
        DownloadData.DownloadUrlList(this, newsSourceUrls, this);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case R.id.settingsButton:
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                break;
            case R.id.refreshMenuButton:
                download();
                break;
            default:
                Log.e(TAG, "onOptionsItemSelected: DEFAULTED");
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings, menu);
        return true;
    }
}
