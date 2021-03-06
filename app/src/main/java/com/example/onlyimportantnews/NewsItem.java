package com.example.onlyimportantnews;

public class NewsItem implements Comparable<NewsItem> {
    private String title;
    private String score;
    private String originUrl;
    private String link;
    private String thumbnail;
    private String subreddit;
    private String uniqueID;
    private Long unixTimeCreated;

    @Override
    public int compareTo(NewsItem itemToCompareTo) {
        if (this.unixTimeCreated > itemToCompareTo.unixTimeCreated) {
            return -1;
        } else if (this.unixTimeCreated < itemToCompareTo.unixTimeCreated) {
            return 1;
        } else {
            return 0;
        }
    }

    public String getSubreddit() {
        return subreddit;
    }

    public void setSubreddit(String subreddit) {
        this.subreddit = subreddit;
    }

    public String getTitle() {
        return title;
    }

    public String getScore() {
        return score;
    }

    public String getOriginUrl() {
        return originUrl;
    }

    public String getLink() {
        return link;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public Long getUnixTimeCreated() {
        return unixTimeCreated;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public void setOriginUrl(String originUrl) {
        this.originUrl = originUrl;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public void setUnixTimeCreated(Long unixTimeCreated) {
        this.unixTimeCreated = unixTimeCreated;
    }

    public String getUniqueID() {
        return uniqueID;
    }

    public void setUniqueID(String uniqueID) {
        this.uniqueID = uniqueID;
    }

}
