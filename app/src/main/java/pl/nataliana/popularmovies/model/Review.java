package pl.nataliana.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Natalia Nazaruk on 25.02.2018.
 */

public class Review implements Parcelable {

    private long id;
    private String author;
    private String review;

    public Review(long id, String author, String review) {

        this.id = id;
        this.author = author;
        this.review = review;

    }

    private Review(Parcel in) {
        id = in.readLong();
        author = in.readString();
        review = in.readString();

    }

    public static final Parcelable.Creator<Review> CREATOR = new Parcelable.Creator<Review>() {
        @Override
        public Review createFromParcel(Parcel in) {
            return new Review(in);
        }

        @Override
        public Review[] newArray(int size) {
            return new Review[size];
        }
    };

    public long getId() {
        return id;
    }

    public String getAuthor() {
        return author;
    }

    public String getReview() {
        return review;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeString(author);
        parcel.writeString(review);
    }
}
