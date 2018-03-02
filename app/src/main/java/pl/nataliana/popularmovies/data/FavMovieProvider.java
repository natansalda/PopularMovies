package pl.nataliana.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

/**
 * Created by Natalia Nazaruk on 01.03.2018.
 */

public class FavMovieProvider extends ContentProvider {

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private FavMovieDbHelper mDbHelper;

    private static final int MOVIES = 100;
    private static final int MOVIES_ID = 101;

    private static UriMatcher buildUriMatcher() {

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = FavMovieContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, FavMovieContract.MOVIES_PATH, MOVIES);
        matcher.addURI(authority, FavMovieContract.MOVIES_PATH + "/#", MOVIES_ID);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mDbHelper = new FavMovieDbHelper(getContext());
        return true;
    }

    @Override
    public String getType(Uri uri) {

        switch (sUriMatcher.match(uri)) {
            case MOVIES:
                return FavMovieContract.MovieEntry.CONTENT_TYPE;
            case MOVIES_ID:
                return FavMovieContract.MovieEntry.CONTENT_ITEM_TYPE;
            default:
                return null;
        }

    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        Cursor movieCursor;
        final int match = sUriMatcher.match(uri);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();

        switch (match) {

            case MOVIES: {
                builder.setTables(FavMovieContract.MovieEntry.TABLE_NAME);
                if (TextUtils.isEmpty(sortOrder)) {
                    sortOrder = FavMovieContract.MovieEntry.SORT_ORDER;
                }
                break;
            }

            case MOVIES_ID: {
                builder.setTables(FavMovieContract.MovieEntry.TABLE_NAME);
                builder.appendWhere(FavMovieContract.MovieEntry.MOVIE_ID + " = " +
                        uri.getLastPathSegment());
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        movieCursor = builder.query(
                db,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder);

        movieCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return movieCursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case MOVIES: {
                long _id = db.insert(FavMovieContract.MovieEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = FavMovieContract.MovieEntry.buildMovieUri(_id);
                else
                    throw new android.database.SQLException("Failed when inserting the row into" + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;

        switch (match) {
            case MOVIES:
                rowsDeleted = db.delete(
                        FavMovieContract.MovieEntry.TABLE_NAME, selection, selectionArgs);
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (selection == null || rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
            case MOVIES:
                rowsUpdated = db.update(
                        FavMovieContract.MovieEntry.TABLE_NAME,
                        values,
                        selection,
                        selectionArgs);
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;

    }
}
