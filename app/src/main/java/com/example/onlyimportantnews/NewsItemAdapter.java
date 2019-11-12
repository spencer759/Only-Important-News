package com.example.onlyimportantnews;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class NewsItemAdapter extends ArrayAdapter {
    private static final String TAG = "NewsItemAdapter";
    private final int layoutResource;
    private final LayoutInflater layoutInflater;
    private List<NewsItem> newsItemList;
    private NewsItem currentNewsItem;
    private Context mContext;

    public NewsItemAdapter(Context context, int resource, List<NewsItem> newsItems) {
        super(context, resource);
        this.layoutResource = resource;
        this.layoutInflater = LayoutInflater.from(context);
        this.newsItemList = newsItems;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return newsItemList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = layoutInflater.inflate(layoutResource, parent, false);

            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        currentNewsItem = newsItemList.get(position);

        TextView newsTitle = convertView.findViewById(R.id.newsTitleTextView);
        TextView newsDate = convertView.findViewById(R.id.newsDateTextView);
        TextView newsCategory = convertView.findViewById(R.id.newsCategoryTextView);

        newsTitle.setText(currentNewsItem.getTitle());
        newsDate.setText(TimeDateConversion.UnixToDayAndMonth(currentNewsItem.getUnixTimeCreated()));
        newsCategory.setText(setCategory());

        setTextClickListener(newsTitle);

        return convertView;
    }

    private String setCategory() {
        switch(currentNewsItem.getSubreddit()) {
            case "worldnews":
                return "World News";
            case "politics":
                return "US Politics";
            case "ukpolitics":
                return "UK Politics";
            case "tech":
                return "Technology";
            case "science":
                return "Science";
            case "space":
                return "Space";
            case "movies":
                return "Movies and Television";
            case "television":
                return "Movies and Television";
            case "Economics":
                return "Economy";
            case "sports":
                return "Sports";
            case "Health":
                return "Health";
        }

        return null;
    }

    private void setTextClickListener(TextView newsTitle) {
        newsTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse(currentNewsItem.getOriginUrl()));
                mContext.startActivity(intent);
            }
        });
    }

    private class ViewHolder {
        final TextView newsTitle;
        final TextView newsDate;
        final TextView newsCategory;

        ViewHolder(View v) {
            this.newsTitle = v.findViewById(R.id.newsTitleTextView);
            this.newsDate = v.findViewById(R.id.newsDateTextView);
            this.newsCategory = v.findViewById(R.id.newsCategoryTextView);

        }
    }
}
