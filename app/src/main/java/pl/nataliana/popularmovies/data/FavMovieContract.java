package pl.nataliana.popularmovies.data;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Natalia Nazaruk on 01.03.2018.
 */

public class FavMovieContract {

    public static final String CONTENT_AUTHORITY = "pl.nataliana.popularmovies";
    public static final Uri BASE_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String MOVIES_PATH = "movies";

    public static final class MovieEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_URI.buildUpon().appendPath(MOVIES_PATH).build();

        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + MOVIES_PATH;
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + MOVIES_PATH;

        public static final String TABLE_NAME = "movies";
        public static final String MOVIE_ID = "id";
        public static final String MOVIE_TITLE = "original_title";
        public static final String MOVIE_OVERVIEW = "overview";
        public static final String MOVIE_RELEASE_DATE = "release_date";
        public static final String MOVIE_POSTER = "poster_path";
        public static final String MOVIE_VOTE_AVERAGE = "vote_average";
        public static final String MOVIE_POPULARITY = "popularity";

        public static final String[] PROJECTION =
                {MOVIE_ID, MOVIE_TITLE, MOVIE_OVERVIEW, MOVIE_RELEASE_DATE, MOVIE_POSTER,
                        MOVIE_VOTE_AVERAGE, MOVIE_POPULARITY};

        public static final String SORT_ORDER =
                MOVIE_VOTE_AVERAGE + " ASC";

        public static Uri buildMovieUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}