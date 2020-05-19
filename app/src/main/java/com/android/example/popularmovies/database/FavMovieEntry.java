package com.android.example.popularmovies.database;

import android.annotation.SuppressLint;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.android.example.popularmovies.utils.JsonUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity(tableName = "movies")
public class FavMovieEntry {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    @ColumnInfo(name = JsonUtils.OVERVIEW)
    private String description;
    @ColumnInfo(name = JsonUtils.VOTE_AVERAGE)
    private double userRating;
    @ColumnInfo(name = JsonUtils.POSTER_PATH)
    private String posterUrl;
    @ColumnInfo(name = JsonUtils.RELEASE_DATE)
    private Date releaseDate;

    @Ignore
    public FavMovieEntry(String title, String description, double userRating, String posterUrl,
                         Date releaseDate) {
        this.title = title;
        this.description = description;
        this.userRating = userRating;
        this.posterUrl = posterUrl;
        this.releaseDate = releaseDate;
    }

    @Ignore
    @SuppressLint("SimpleDateFormat")
    public FavMovieEntry(String title, String description, double userRating, String posterUrl,
                         String releaseDateString) throws ParseException {
        this.title = title;
        this.description = description;
        this.userRating = userRating;
        this.posterUrl = posterUrl;
        //Parses date string passed by API
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        releaseDate = format.parse(releaseDateString);
    }

    public FavMovieEntry(int id, String title, String description, double userRating, String posterUrl,
                         Date releaseDate) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.userRating = userRating;
        this.posterUrl = posterUrl;
        this.releaseDate = releaseDate;
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
}
