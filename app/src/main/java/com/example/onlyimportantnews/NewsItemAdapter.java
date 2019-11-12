package com.example.onlyimportantnews;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

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

        TextView newsTitle = convertView.findViewById(R.id.newsTitle);
        TextView newsDate = convertView.findViewById(R.id.newsDate);

        newsTitle.setText(currentNewsItem.getTitle());
        newsDate.setText(TimeDateConversion.UnixToDayAndMonth(currentNewsItem.getUnixTimeCreated()));

        setTextClickListener(newsTitle);

        setThumbnailImage(convertView);

        return convertView;
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

    private void setThumbnailImage(View convertView) {
        if (currentNewsItem.getThumbnail().isEmpty()) {
            // put local image later
            Picasso.get().load("https://image.flaticon.com/icons/png/128/149/149366.png").into((ImageView) convertView.findViewById(R.id.newsThumbnail));
        } else {
            Picasso.get().load(currentNewsItem.getThumbnail()).into((ImageView) convertView.findViewById(R.id.newsThumbnail));
        }
    }

    private class ViewHolder {
        final TextView newsTitle;
        final TextView newsDate;
        final ImageView newsImage;

        ViewHolder(View v) {
            this.newsTitle = v.findViewById(R.id.newsTitle);
            this.newsDate = v.findViewById(R.id.newsDate);
            this.newsImage = v.findViewById(R.id.newsThumbnail);

        }
    }
}