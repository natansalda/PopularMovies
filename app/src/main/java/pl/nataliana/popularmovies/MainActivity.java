package pl.nataliana.popularmovies;

import android.content.Context;
import android.content.Intent;
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
import pl.nataliana.movieapp.R;
import pl.nataliana.popularmovies.adapters.MovieAdapter;
import pl.nataliana.popularmovies.model.Movie;

/**
 * Created by Natalia Nazaruk on 17.02.2018.
 */

public class MainActivity extends AppCompatActivity {

    // My application constans. Please change MY_API_KEY to your API.
    public static final String MY_API_KEY = ""; //Please put your API key here
    public static final String SINGLE_MOVIE_BASE_URL = "http://api.themoviedb.org/3/movie/%s?api_key=" + MY_API_KEY;
    public static final String MOVIE_POSTER_BASE_URL = "http://image.tmdb.org/t/p/";
    public static final String POSTER_SIZE = "w185";
    private static final String TAG = MainActivity.class.getSimpleName();
    private String mChoosenOption = "popularity";
    private MovieAdapter movieAdapter;
    public GridView gridView;

    // ArrayList of movies
    ArrayList<Movie> movies = new ArrayList<>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (MY_API_KEY.isEmpty()) {
            Toast.makeText(getApplicationContext(), R.string.obtain_api_key, Toast.LENGTH_LONG).show();
            return;
        }

        movieAdapter = new MovieAdapter(this, new ArrayList<Movie>());
        gridView = findViewById(R.id.gridview);

        if (savedInstanceState == null) {
            showPosters(mChoosenOption);
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
        }

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Movie movie = movieAdapter.getItem(position);
                //Toast.makeText(getActivity(), "Movie title:" + movie.title, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra(getResources().getString(R.string.movie_parcelable), (Parcelable) movie);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //Log.v(LOG_TAG, "onSaveInstanceState");

        int numMovieObjects = gridView.getCount();
        if (numMovieObjects > 0) {
            // Get Movie objects from gridview
            Movie[] movies = new Movie[numMovieObjects];
            for (int i = 0; i < numMovieObjects; i++) {
                movies[i] = (Movie) gridView.getItemAtPosition(i);
            }

            // Save Movie objects to bundle
            outState.putParcelableArray(getString(R.string.movie_parcelable), (Parcelable[]) movies);
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
        if (id == R.id.sort_by_popularity && !mChoosenOption.equals("popularity")) {
            Context context = MainActivity.this;
            mChoosenOption = "popularity";
            showPosters(mChoosenOption);
            String sortMessage = "Sort by rating selected";
            Toast.makeText(context, sortMessage, Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.sort_by_rating && !mChoosenOption.equals("rating")) {
            Context context = MainActivity.this;
            mChoosenOption = "rating";
            showPosters(mChoosenOption);
            String sortMessage = "Sort by popularity selected";
            Toast.makeText(context, sortMessage, Toast.LENGTH_SHORT).show();
            return true;
        } else
            return super.onOptionsItemSelected(item);
    }

    private String getSort(String sort) {
        if (sort.equals("popularity"))
            return "popular";
        else if (sort.equals("rating"))
            return "top_rated";
        return "popular";
    }

    private void showPosters(String sort) {
        AsyncHttpClient client = new AsyncHttpClient();
        String requestUrl = String.format(SINGLE_MOVIE_BASE_URL, getSort(sort), MY_API_KEY);
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
                                movie.getString("popularity"),
                                movie.getString("vote_average"));

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
            }


            @Override
            public void onFailure(int statusCode, Header[] headers, String responseBody, Throwable error) {
                Toast.makeText(MainActivity.this, "Error loading movies", Toast.LENGTH_SHORT).show();
            }

        });
    }
}