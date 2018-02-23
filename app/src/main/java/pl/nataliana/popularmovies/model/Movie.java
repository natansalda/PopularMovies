package pl.nataliana.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Natalia Nazaruk on 19.02.2018.
 */

public class Movie {

    private long id;
    private String title;
    private String synopsis;
    private String date;
    private String poster;
    private String rating;
    private String popularity;

    public Movie(long id, String title, String synopsis, String date, String poster, String rating, String popularity) {

        this.id = id;
        this.title = title;
        this.synopsis = synopsis;
        this.date = date;
        this.poster = poster;
        this.rating = rating;
        this.popularity = popularity;
    }

    public Movie(String poster, long id) {
        this.poster = poster;
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public String getDate() {
        return date;
    }

    public String getPoster() {
        return poster;
    }

    public String getRating() {
        return rating;
    }

    public String getPopularity() {
        return popularity;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(poster);
        dest.writeString(synopsis);
        dest.writeString(rating);
        dest.writeString(date);
    }

    private Movie(Parcel in) {
        title = in.readString();
        poster = in.readString();
        synopsis = in.readString();
        rating = (String) in.readValue(Double.class.getClassLoader());
        date = in.readString();
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
