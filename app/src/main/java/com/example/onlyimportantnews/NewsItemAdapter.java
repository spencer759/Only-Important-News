package com.example.onlyimportantnews;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

import java.util.List;
import java.util.prefs.Preferences;

public class NewsItemAdapter extends ArrayAdapter {

    private static final String TAG = "NewsItemAdapter";
    private final int layoutResource;
    private final LayoutInflater layoutInflater;
    private List<NewsItem> newsItemList;
    private NewsItem currentNewsItem;
    private Context mContext;

    private int imageWidthBoundary, imageHeightBoundary, screenWidth;

    private ViewHolder newsItemViewHolder;

    Preferences seenNewsItems = Preferences.userNodeForPackage(this.getClass());

    public NewsItemAdapter(Context context, int resource, List<NewsItem> newsItems) {
        super(context, resource);
        this.layoutResource = resource;
        this.layoutInflater = LayoutInflater.from(context);
        this.newsItemList = newsItems;
        this.mContext = context;
        setNewsThumbnailWidthAndHeight();
    }

    private void setNewsThumbnailWidthAndHeight() {
        screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
        imageWidthBoundary =  (int)((double) screenWidth * 0.9);
        imageHeightBoundary = (int)(imageWidthBoundary * 0.6);
    }

    @Override
    public int getCount() {
        return newsItemList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = layoutInflater.inflate(layoutResource, parent, false);
            newsItemViewHolder = new ViewHolder(convertView);
            convertView.setTag(newsItemViewHolder);
        } else {
            newsItemViewHolder = (ViewHolder) convertView.getTag();
        }

        currentNewsItem = newsItemList.get(position);

        setNewsLayoutElements();

        linkNewsItemToUrl(convertView);

        return convertView;
    }

    private class ViewHolder {
        final TextView newsTitle;
        final TextView newsDate;
        final TextView newsCategory;
        final ImageView newsImage;

        ViewHolder(View v) {
            this.newsTitle = v.findViewById(R.id.newsTitleTextView);
            this.newsDate = v.findViewById(R.id.hoursSincePostedTextView);
            this.newsCategory = v.findViewById(R.id.newsCategoryTextView);
            this.newsImage = v.findViewById(R.id.newsThumbnailImageView);

        }
    }

    private void setNewsLayoutElements() {
        newsItemViewHolder.newsTitle.setText(currentNewsItem.getTitle());
        newsItemViewHolder.newsDate.setText(TimeDateConversion.unixTimeToDaysAndHours(currentNewsItem.getUnixTimeCreated()));
        newsItemViewHolder.newsCategory.setText(setCategory());
        setThumbnailImage(currentNewsItem.getThumbnail(), newsItemViewHolder.newsImage, newsItemViewHolder.newsTitle);
    }

    private void setThumbnailImage(String imageUrl, final ImageView image, final TextView newsTitle) {
        Picasso.get().load(imageUrl).transform(new RoundedCornersTransformation(12,12))
                .centerCrop()
                .resize(imageWidthBoundary, imageHeightBoundary)
                .into(image);
    }

    private void linkNewsItemToUrl(View convertView) {
        final String newsUrl = currentNewsItem.getOriginUrl();
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse(newsUrl));
                mContext.startActivity(intent);
            }
        });
    }

    private void removeImageFromNewsItem(ImageView imageRef, TextView newsTitle) {
        imageRef.setVisibility(View.GONE);
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) newsTitle.getLayoutParams();
        params.width = ConstraintLayout.LayoutParams.MATCH_PARENT;
        newsTitle.setLayoutParams(params);
    }

    private String setCategory() {
        switch(currentNewsItem.getSubreddit()) {
            case "GlobalNews":
                return "World News";
            case "politics":
                return "US Politics";
            case "BritishPolitics":
                return "UK Politics";
            case "technews":
                return "Technology";
            case "science":
                return "Science";
            case "space":
                return "Space";
            case "movies":
                return "Movies and Television";
            case "television":
                return "Movies and Television";
            case "economy":
                return "Economy";
            case "sports":
                return "Sports";
            case "Health":
                return "Health";
        }

        return null;
    }
}
