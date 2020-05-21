package com.android.example.popularmovies.utils.json;

import android.net.Uri;

public class MovieReview {

    private String author;
    private String content;
    private String id;
    private String url;

    public MovieReview(String author, String content, String id, String url) {
        this.author = author;
        this.content = content;
        this.id = id;
        this.url = url;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public Uri getUrlAsUri(){
        return Uri.parse(url);
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
