package com.android.example.popularmovies.utils.json;

import android.net.Uri;

public class MovieVideo {
    private static final String YOUTUBE_URL_BASE = "https://www.youtube.com/watch?v=";

    private String id;
    private String name;
    private String key;
    private int size;
    private String type;
    private String site;
    private String iso_639_1;

    public MovieVideo(String id, String name, String key, int size, String type, String site, String iso_639_1, String iso_3166_1) {
        this.id = id;
        this.name = name;
        this.key = key;
        this.size = size;
        this.type = type;
        this.site = site;
        this.iso_639_1 = iso_639_1;
        this.iso_3166_1 = iso_3166_1;
    }

    private String iso_3166_1;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getIso_639_1() {
        return iso_639_1;
    }

    public void setIso_639_1(String iso_639_1) {
        this.iso_639_1 = iso_639_1;
    }

    public String getIso_3166_1() {
        return iso_3166_1;
    }

    public void setIso_3166_1(String iso_3166_1) {
        this.iso_3166_1 = iso_3166_1;
    }

    public Uri getTrailerUri(){
        if (site.equals("YouTube")){
            return Uri.parse(YOUTUBE_URL_BASE+key);
        } //TODO: Parse Uris for sites other than YouTube
        throw new RuntimeException("Parsing videos from sites other than YouTube not implemented");
    }
}
