package com.android.example.popularmovies.utils.json;

import android.net.Uri;

import com.squareup.moshi.FromJson;
import com.squareup.moshi.ToJson;

import org.json.JSONException;
import org.json.JSONObject;

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

        @FromJson
        MovieVideo videoFromJson(JSONObject videoJSON) throws JSONException {
            MovieVideo video = new MovieVideo();
            video.setIsoIdentifier(videoJSON.getString(ISO_LANG)+'_'
                    +videoJSON.getString(ISO_COUNTRY));
            video.setId(videoJSON.getString(ID));
            video.setKey(videoJSON.getString(KEY));
            video.setName(videoJSON.getString(NAME));
            video.setSite(videoJSON.getString(SITE));
            video.setSize(videoJSON.getInt(SIZE));
            video.setType(videoJSON.getString(TYPE));
            return video;
        }

        @ToJson
        JSONObject videoToJson(MovieVideo video) throws JSONException {
            JSONObject jsonObject = new JSONObject();
            String[] isoIdentifiers = video.getIsoIdentifier().split("_");
            jsonObject.put(ID, video.getId());
            jsonObject.put(ISO_LANG, isoIdentifiers[0]);
            jsonObject.put(ISO_COUNTRY, isoIdentifiers[1]);
            jsonObject.put(KEY, video.getKey());
            jsonObject.put(NAME, video.getName());
            jsonObject.put(SITE, video.getSite());
            jsonObject.put(SIZE, video.getSize());
            jsonObject.put(TYPE, video.getType());
            return jsonObject;
        }
    }
}
