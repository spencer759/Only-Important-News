package com.example.onlyimportantnews;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DownloadData {

        private static ListView newsListView;
        private static SwipeRefreshLayout swipeToLoad;
        private static List<NewsItem> newsItemsDownloaded = new ArrayList<NewsItem>();
        private static Context mContext;
        private static final List<String> urlsToBlock = new ArrayList<>();

    public static void DownloadUrlList(Context context, ArrayList<String> urlList, Activity activity){
        mContext = context;
        newsListView = activity.findViewById(R.id.newsListView);
        swipeToLoad = activity.findViewById(R.id.swipeRefresh);

        newsItemsDownloaded.clear();
        urlsToBlock.clear();

        urlsToBlock.add("redd.it");
        urlsToBlock.add("imgur.com");
        urlsToBlock.add("twitter.com");
        urlsToBlock.add("reddit.com");
        urlsToBlock.add("youtube.com");
        urlsToBlock.add("gfycat.com");
        urlsToBlock.add("giphy.com");
        urlsToBlock.add("twitch.com");
        urlsToBlock.add("streamable.com");
        urlsToBlock.add("facebook.com");
        urlsToBlock.add("instagram.com");

        new GetDataInBackground().execute(urlList);
    }

        public static class GetDataInBackground extends AsyncTask<ArrayList<String>, Void, String> {
            private final String TAG = "DownloadData";
            private String title ,originUrl, isLink, thumbnail;
            private Long unixTimeCreated;
            private boolean containsBlockedUrl = false;

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                swipeToLoad.setRefreshing(false);

                Collections.sort(newsItemsDownloaded);

                NewsItemAdapter newsAdapter = new NewsItemAdapter(mContext, R.layout.news_item, newsItemsDownloaded);
                newsListView.setAdapter(newsAdapter );
            }

            @Override
            protected String doInBackground(ArrayList<String>... urls) {
                for (String url : urls[0]) {
                    String newsJsonData = downloadTextFromURL(url);
                    processNewsItems(newsJsonData);
                }
                return "Downloaded Data";
            }

            private String processNewsItems(String data) {
                try {
                    JSONArray jsonArr = new JSONObject(data).getJSONObject("data").getJSONArray("children");

                    for (int i = 0; i <  jsonArr.length(); i ++) {
                        unixTimeCreated = jsonArr.getJSONObject(i).getJSONObject("data").getLong("created");
                        long unixTime = System.currentTimeMillis() / 1000L;

                        if (unixTimeCreated < unixTime - 604800) {
                            continue;
                        }

                        originUrl = jsonArr.getJSONObject(i).getJSONObject("data").getString("url");
                        for (String url : urlsToBlock) {
                            if (originUrl.contains(url)) {
                                containsBlockedUrl = true;
                                break;
                            }
                        }
                        if(containsBlockedUrl) {
                            containsBlockedUrl = false;
                            continue;
                        }

                        title = jsonArr.getJSONObject(i).getJSONObject("data").getString("title");
                        isLink = jsonArr.getJSONObject(i).getJSONObject("data").getString("is_self");
                        thumbnail = jsonArr.getJSONObject(i).getJSONObject("data").getString("thumbnail");

                        if (isLink == "false") {
                            NewsItem tmpNewsItem = new NewsItem();
                            tmpNewsItem.setTitle(title);
                            tmpNewsItem.setLink(isLink);
                            tmpNewsItem.setThumbnail(thumbnail);
                            tmpNewsItem.setOriginUrl(originUrl);
                            tmpNewsItem.setUnixTimeCreated(unixTimeCreated);

                            newsItemsDownloaded.add(tmpNewsItem);
                        }
                    }
                } catch(Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            private String downloadTextFromURL(String urlString) {
                try{
                    URL url = new URL(urlString);

                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder sb = new StringBuilder();

                    String line;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                    }

                    reader.close();

                    return sb.toString();
                } catch(Exception e){
                    e.printStackTrace();
                }
                return null;
            }
        }
}