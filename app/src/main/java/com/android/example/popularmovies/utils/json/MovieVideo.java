package com.android.example.popularmovies.utils.json;

import android.net.Uri;

import com.squareup.moshi.FromJson;
import com.squareup.moshi.ToJson;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MovieVideo {
    private static final String YOUTUBE_URL_BASE = "https://www.youtube.com/watch?v=";

    private String id;
    private String name;
    private String key;
    private int size;
    private String type;
    private String site;
    private String isoIdentifier;



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

    public String getIsoIdentifier() {
        return isoIdentifier;
    }

    public void setIsoIdentifier(String isoIdentifier) {
        this.isoIdentifier = isoIdentifier;
    }

    public Uri getVideoUri(){
        if (site.equals("YouTube")){
            return Uri.parse(YOUTUBE_URL_BASE+key);
        } //TODO: Parse Uris for sites other than YouTube
        throw new RuntimeException("Parsing videos from sites other than YouTube not implemented");
    }

    public static class VideoAdapter{


        static final String ISO_LANG = "iso_639_1";
        static final String ISO_COUNTRY = "iso_3166_1";
        static final String ID = "id";
        static final String KEY = "key";
        static final String NAME = "name";
        static final String SITE = "site";
        static final String SIZE = "size";
        static final String TYPE = "type";
        static final char ISO_DIVIDER = '_';

        @FromJson
        MovieVideo videoFromJson(Map<String, String> videoMap) {
            MovieVideo video = new MovieVideo();
            video.setIsoIdentifier(videoMap.get(ISO_LANG)+ ISO_DIVIDER
                    +videoMap.get(ISO_COUNTRY));
            video.setId(videoMap.get(ID));
            video.setKey(videoMap.get(KEY));
            video.setName(videoMap.get(NAME));
            video.setSite(videoMap.get(SITE));
            video.setSize(Integer.parseInt(Objects.requireNonNull(videoMap.get(SIZE))));
            video.setType(videoMap.get(TYPE));
            return video;
        }

        @ToJson
        Map<String, String> videoToJson(MovieVideo video) throws JSONException {
            HashMap<String, String> map = new HashMap<>();
            String[] isoIdentifiers = video.getIsoIdentifier().split(String.valueOf(ISO_DIVIDER));
            map.put(ID, video.getId());
            map.put(ISO_LANG, isoIdentifiers[0]);
            map.put(ISO_COUNTRY, isoIdentifiers[1]);
            map.put(KEY, video.getKey());
            map.put(NAME, video.getName());
            map.put(SITE, video.getSite());
            map.put(SIZE, String.valueOf(video.getSize()));
            map.put(TYPE, video.getType());
            return map;
        }
    }
}
