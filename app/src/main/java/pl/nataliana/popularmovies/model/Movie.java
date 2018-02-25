package pl.nataliana.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Natalia Nazaruk on 19.02.2018.
 */

public class Movie implements Parcelable {

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

    private Movie(Parcel in) {
        id = in.readLong();
        title = in.readString();
        synopsis = in.readString();
        date = in.readString();
        poster = in.readString();
        rating = in.readString();
        popularity = in.readString();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeString(title);
        parcel.writeString(synopsis);
        parcel.writeString(date);
        parcel.writeString(poster);
        parcel.writeString(rating);
        parcel.writeString(popularity);
    }

    public String getPosterUrl() {
        final String TMDB_POSTER_BASE_URL = "https://image.tmdb.org/t/p/w185";

        return TMDB_POSTER_BASE_URL + poster;
    }
}