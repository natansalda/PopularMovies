package pl.nataliana.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Natalia Nazaruk on 01.03.2018.
 */

public class FavMovieDbHelper extends SQLiteOpenHelper {

    private  static  final int DATABASE_VERSION = 1;

    static final String DATABASE_NAME = "favMovies.db";

    public FavMovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " + FavMovieContract.MovieEntry.TABLE_NAME + " (" +
                FavMovieContract.MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                FavMovieContract.MovieEntry.MOVIE_ID + " TEXT UNIQUE NOT NULL," +
                FavMovieContract.MovieEntry.MOVIE_BACKDROP_URI + " TEXT NOT NULL," +
                FavMovieContract.MovieEntry.MOVIE_TITLE + " TEXT NOT NULL," +
                FavMovieContract.MovieEntry.MOVIE_POSTER + " TEXT NOT NULL," +
                FavMovieContract.MovieEntry.MOVIE_OVERVIEW + " TEXT NOT NULL," +
                FavMovieContract.MovieEntry.MOVIE_VOTE_AVERAGE + " TEXT NOT NULL," +
                FavMovieContract.MovieEntry.MOVIE_RELEASE_DATE + " TEXT NOT NULL," +
                "UNIQUE (" + FavMovieContract.MovieEntry.MOVIE_ID +") ON CONFLICT IGNORE"+
                " );";

        db.execSQL(SQL_CREATE_MOVIE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + FavMovieContract.MovieEntry.TABLE_NAME);
        onCreate(db);
    }
}
