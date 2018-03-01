package pl.nataliana.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Natalia Nazaruk on 28.02.2018.
 */

public class Trailer implements Parcelable {

    private String id;
    private String key;

    public Trailer(String id, String key) {

        this.id = id;
        this.key = key;
    }

    private Trailer(Parcel in) {
        id = in.readString();
        key = in.readString();
    }

    public static final Parcelable.Creator<Trailer> CREATOR = new Parcelable.Creator<Trailer>() {
        @Override
        public Trailer createFromParcel(Parcel in) {
            return new Trailer(in);
        }

        @Override
        public Trailer[] newArray(int size) {
            return new Trailer[size];
        }
    };

    public String getId() {
        return id;
    }

    public String getKey() {
        return key;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(key);
    }
}
