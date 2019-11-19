package com.example.onlyimportantnews;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.prefs.*;
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
import java.util.prefs.Preferences;

public class DownloadData {

        private static ListView newsListView;
        private static SwipeRefreshLayout swipeToLoad;
        private static List<NewsItem> newsItemsDownloaded = new ArrayList<NewsItem>();
        private static Context mContext;
        private static final List<String> urlsToBlock = new ArrayList<>();
        private static ProgressBar mainProgressBar;

    public static void DownloadUrlList(Context context, ArrayList<String> urlList, Activity activity){
        mContext = context;
        newsListView = activity.findViewById(R.id.newsListView);
        swipeToLoad = activity.findViewById(R.id.swipeRefresh);
        mainProgressBar = activity.findViewById(R.id.progressBar);

        newsItemsDownloaded.clear();

        addUrlsToBlock();

        new GetDataInBackground().execute(urlList);
    }

    private static void addUrlsToBlock(){
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
    }

        public static class GetDataInBackground extends AsyncTask<ArrayList<String>, Void, String> {
            private final String TAG = "DownloadData";
            private String title ,originUrl, isLink, thumbnail, subreddit, uniqueID;
            private Long unixTimeCreated;
            private JSONArray newsItemsJsonArray;
            long unixTime;

            @Override
            protected String doInBackground(ArrayList<String>... urlList) {
                for (String url : urlList[0]) {
                    transformTextIntoNewsItems(downloadTextFromURL(url));
                }
                return "Downloaded Data";
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

            private void transformTextIntoNewsItems(String data) {
                try {
                    newsItemsJsonArray = new JSONObject(data).getJSONObject("data").getJSONArray("children");

                    for (int i = 0; i <  newsItemsJsonArray.length(); i ++) {

                        setNewsItemAttributes(i);

                        if (isNewsItemMoreThanOneWeekOld() || (urlContainsUrlToBlock())) {
                            continue;
                        }

                        checkAmpersandFailure();

                        if (isLink == "false") {
                            NewsItem tempNewsItem = createNewsItem();
                            newsItemsDownloaded.add(tempNewsItem);
                        }
                    }
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }

            private void setNewsItemAttributes(int arrayIndex) throws Exception {
                unixTimeCreated = newsItemsJsonArray.getJSONObject(arrayIndex).getJSONObject("data").getLong("created");
                originUrl = newsItemsJsonArray.getJSONObject(arrayIndex).getJSONObject("data").getString("url");
                title = newsItemsJsonArray.getJSONObject(arrayIndex).getJSONObject("data").getString("title");
                isLink = newsItemsJsonArray.getJSONObject(arrayIndex).getJSONObject("data").getString("is_self");
                thumbnail = newsItemsJsonArray.getJSONObject(arrayIndex).getJSONObject("data").getString("thumbnail");
                subreddit = newsItemsJsonArray.getJSONObject(arrayIndex).getJSONObject("data").getString("subreddit");
                uniqueID = newsItemsJsonArray.getJSONObject(arrayIndex).getJSONObject("data").getString("name");
            }

            private boolean isNewsItemMoreThanOneWeekOld() {
                unixTime = System.currentTimeMillis() / 1000L;
                return unixTimeCreated < unixTime - 604800;
            }

            private boolean urlContainsUrlToBlock() {
                for (String url : urlsToBlock) {
                    if (originUrl.contains("thumbs.redditmedia.com")) {
                        return false;
                    }

                    if (originUrl.contains(url)) {
                        return true;
                    }
                }
                return false;
            }

            private void checkAmpersandFailure() {
                if (title.contains("&amp;")) {
                    title.replace("&amp;", "&");
                }
            }

            private NewsItem createNewsItem() {
                NewsItem tempNewsItem = new NewsItem();
                tempNewsItem.setTitle(title);
                tempNewsItem.setLink(isLink);
                tempNewsItem.setThumbnail(thumbnail);
                tempNewsItem.setOriginUrl(originUrl);
                tempNewsItem.setUnixTimeCreated(unixTimeCreated);
                tempNewsItem.setSubreddit(subreddit);
                return tempNewsItem;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                swipeToLoad.setRefreshing(false);
                mainProgressBar.setVisibility(View.GONE);

                Collections.sort(newsItemsDownloaded);

                newsListView.setAdapter(new NewsItemAdapter(mContext, R.layout.news_item, newsItemsDownloaded));
            }
        }
}