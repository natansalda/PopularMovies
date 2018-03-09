package pl.nataliana.popularmovies;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import pl.nataliana.popularmovies.adapters.MovieAdapter;
import pl.nataliana.popularmovies.data.FavMovieContract;
import pl.nataliana.popularmovies.model.Movie;

/**
 * Created by Natalia Nazaruk on 17.02.2018.
 */

public class MainActivity extends AppCompatActivity {

    // My application constans. Please change MY_API_KEY to your API.
    public static final String API_KEY = BuildConfig.API_KEY; //Please add your proper API key to project:gradle.properties file
    public static final String SINGLE_MOVIE_BASE_URL = "http://api.themoviedb.org/3/movie/%s?api_key=" + API_KEY;
    public static final String MOVIE_POSTER_BASE_URL = "http://image.tmdb.org/t/p/";
    public static final String POSTER_SIZE = "w185";
    private static final String TAG = MainActivity.class.getSimpleName();
    private String mChoosenOption = "";
    private MovieAdapter movieAdapter;
    public GridView gridView;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (API_KEY.isEmpty()) {
            Toast.makeText(getApplicationContext(), R.string.obtain_api_key, Toast.LENGTH_LONG).show();
            return;
        }

        movieAdapter = new MovieAdapter(this, new ArrayList<Movie>());
        gridView = findViewById(R.id.gridview);

        if (savedInstanceState == null) {
            showPosters("popular");
        } else {
            // Get data from local resources
            // Get Movie objects
            Parcelable[] parcelable = savedInstanceState.
                    getParcelableArray(getString(R.string.movie_parcelable));

            if (parcelable != null) {
                int numMovieObjects = parcelable.length;
                Movie[] movies = new Movie[numMovieObjects];
                for (int i = 0; i < numMovieObjects; i++) {
                    movies[i] = (Movie) parcelable[i];
                }

                // Load movie objects into view
                gridView.setAdapter(movieAdapter);
            }

            String savedOption = savedInstanceState.getString("mChoosenOption");
            if (savedOption.equals("favorites")) {
                mChoosenOption = savedOption;
                showFavorites();
            } else {
                mChoosenOption = savedOption;
                showPosters(savedOption);
            }
        }

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Movie movie = movieAdapter.getItem(position);
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                Long MovieID = movie.getId();
                intent.putExtra(getString(R.string.movie_id_extras), MovieID);
                intent.putExtra(getString(R.string.movie_parcelable), movie);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        int numMovieObjects = gridView.getCount();
        if (numMovieObjects > 0) {
            // Get Movie objects from gridview
            Movie[] movies = new Movie[numMovieObjects];
            for (int i = 0; i < numMovieObjects; i++) {
                movies[i] = (Movie) gridView.getItemAtPosition(i);
            }

            // Save Movie objects to bundle
            outState.putParcelableArray(getString(R.string.movie_parcelable), movies);
            outState.putString("mChoosenOption", mChoosenOption);
        }

        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Inflate the menu with menu_main.xml
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //Sort posters depending on what user clicks
        if (id == R.id.sort_by_popularity) {
            Context context = MainActivity.this;
            mChoosenOption = "popular";
            showPosters(mChoosenOption);
            String sortMessage = "Sort by popularity selected";
            Toast.makeText(context, sortMessage, Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.sort_by_rating) {
            Context context = MainActivity.this;
            mChoosenOption = "top_rated";
            showPosters(mChoosenOption);
            String sortMessage = "Sort by rating selected";
            Toast.makeText(context, sortMessage, Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.sort_by_fav) {
            Context context = MainActivity.this;
            mChoosenOption = "favorites";
            showFavorites();
            String sortMessage = "Sort by favorites selected";
            Toast.makeText(context, sortMessage, Toast.LENGTH_SHORT).show();
            return true;
        } else
            return super.onOptionsItemSelected(item);
    }

    private void showPosters(String sort) {
        AsyncHttpClient client = new AsyncHttpClient();
        String requestUrl = String.format(SINGLE_MOVIE_BASE_URL, getSort(sort), API_KEY);
        client.get(requestUrl, new TextHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseBody) {
                try {
                    JSONObject jsonObj = new JSONObject(responseBody);
                    JSONArray movies = jsonObj.getJSONArray("results");

                    // looping through movies
                    Movie[] movieList = new Movie[20];
                    for (int i = 0; i < movies.length(); ++i) {
                        JSONObject movie = movies.getJSONObject(i);
                        movieList[i] = new Movie(
                                movie.getLong("id"),
                                movie.getString("original_title"),
                                movie.getString("overview"),
                                movie.getString("release_date"),
                                movie.getString("poster_path"),
                                movie.getString("vote_average"),
                                movie.getString("popularity"));

                    }
                    movieAdapter.clear();
                    for (Movie movie : movieList) {
                        if (movie != null) {
                            Log.v(TAG, "POST EXECUTE IMAGE URLS" + movie);
                            movieAdapter.add(movie);
                        }
                    }
                    movieAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    Log.e(TAG, "Error", e);
                }
                gridView.setAdapter(movieAdapter);
                movieAdapter.notifyDataSetChanged();
            }


            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("Failed: ", "" + statusCode);
                Log.d("Error : ", "" + throwable);
            }

        });
    }

    private void showFavorites() {
        Uri uri = FavMovieContract.MovieEntry.CONTENT_URI;
        ContentResolver resolver = getContentResolver();
        Cursor cursor = null;

        try {

            cursor = resolver.query(
                    uri,
                    null,
                    null,
                    null,
                    null);

            movieAdapter.clear();

            if (cursor.moveToFirst()) {
                do {
                    Movie movie = new Movie(cursor.getInt(1), cursor.getString(2),
                            cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7));
                    movieAdapter.add(movie);
                } while (cursor.moveToNext());
            }

        } finally {

            if (cursor != null)
                cursor.close();

        }
    }

    private String getSort(String sort) {
        if (sort.equals("popular"))
            return "popular";
        else if (sort.equals("top_rated"))
            return "top_rated";
        return "popular";
    }
}