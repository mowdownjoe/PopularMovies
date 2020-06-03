package com.android.example.popularmovies.database;

import android.annotation.SuppressLint;
import android.net.Uri;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.android.example.popularmovies.ui.PosterSizes;
import com.android.example.popularmovies.utils.json.JsonUtils;
import com.squareup.moshi.Json;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

@Entity(tableName = "movies")
public class MovieEntry {

    private static final String DATE_PATTERN = "yyyy-MM-dd";
    static final String ID = "id";
    static final String TITLE = "title";

    @PrimaryKey(autoGenerate = false) //Will use ID from API
    @Json(name = ID)
    private int id;
    @Json(name = TITLE)
    private String title;
    @ColumnInfo(name = JsonUtils.OVERVIEW)
    @Json(name = JsonUtils.OVERVIEW)
    private String description;
    @ColumnInfo(name = JsonUtils.VOTE_AVERAGE)
    @Json(name = JsonUtils.VOTE_AVERAGE)
    private double userRating;
    @ColumnInfo(name = JsonUtils.POSTER_PATH)
    @Json(name = JsonUtils.POSTER_PATH)
    private String posterUrl;
    @ColumnInfo(name = JsonUtils.RELEASE_DATE)
    @Json(name = JsonUtils.RELEASE_DATE)
    private Date releaseDate;


    public MovieEntry(int id, String title, String description, double userRating, String posterUrl,
                      Date releaseDate) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.userRating = userRating;
        this.posterUrl = posterUrl;
        this.releaseDate = releaseDate;
    }

    @Ignore
    @SuppressLint("SimpleDateFormat")
    public MovieEntry(int id, String title, String description, double userRating, String posterUrl,
                      String releaseDateString) throws ParseException {
        this.id = id;
        this.title = title;
        this.description = description;
        this.userRating = userRating;
        this.posterUrl = posterUrl;
        //Parses date string passed by API
        SimpleDateFormat format = new SimpleDateFormat(DATE_PATTERN);
        releaseDate = format.parse(releaseDateString);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getUserRating() {
        return userRating;
    }

    public void setUserRating(double userRating) {
        this.userRating = userRating;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MovieEntry that = (MovieEntry) o;
        return id == that.id &&
                Double.compare(that.userRating, userRating) == 0 &&
                Objects.equals(title, that.title) &&
                Objects.equals(description, that.description) &&
                Objects.equals(posterUrl, that.posterUrl) &&
                Objects.equals(releaseDate, that.releaseDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, description, userRating, posterUrl, releaseDate);
    }

    public Uri getPosterUri(PosterSizes size){
        if (size == PosterSizes.ORIGINAL){
            return Uri.parse(JsonUtils.BASE_POSTER_PATH + "original" + posterUrl);
        } else {
            return Uri.parse(JsonUtils.BASE_POSTER_PATH + 'w' + size.size + posterUrl);
        }
    }

}
