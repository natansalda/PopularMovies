package pl.nataliana.popularmovies;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import pl.nataliana.popularmovies.adapters.TrailerAdapter;
import pl.nataliana.popularmovies.model.Trailer;
import pl.nataliana.popularmovies.model.json.JsonConverterFacade;

/**
 * Created by Natalia Nazaruk on 28.02.2018.
 */

public class TrailerActivity extends Activity {

    public static final String API_KEY = BuildConfig.API_KEY; //Please add your proper API key to project:gradle.properties file
    private static final String TAG = TrailerActivity.class.getSimpleName();
    public static final String YOUTUBE_BASE_URL = "https://www.youtube.com/watch?v=";
    public static final String SINGLE_MOVIE_TRAILER_URL = "https://api.themoviedb.org/3/movie/%s/videos?api_key=" + API_KEY;
    private TrailerAdapter trailerAdapter;
    public ListView trailersView;
    public long movieID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trailer);

        trailerAdapter = new TrailerAdapter(this, new ArrayList<Trailer>());
        trailersView = findViewById(R.id.list);

        movieID = getIntent().getExtras().getLong(getString(R.string.movie_id));

        if (savedInstanceState == null) {
            showTrailers();
        } else {
            Parcelable[] parcelable = savedInstanceState.
                    getParcelableArray(getString(R.string.trailers_parcelable));

            if (parcelable != null) {
                // Load trailer objects into view
                trailersView.setAdapter(trailerAdapter);
            }
        }


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        int numTrailerObjects = trailersView.getCount();
        if (numTrailerObjects > 0) {
            Trailer[] trailers = new Trailer[numTrailerObjects];
            for (int i = 0; i < numTrailerObjects; i++) {
                trailers[i] = (Trailer) trailersView.getItemAtPosition(i);
            }

            // Save Trailer objects to bundle
            outState.putParcelableArray(getString(R.string.trailers_parcelable), trailers);
        }

        super.onSaveInstanceState(outState);
        Toast.makeText(getApplicationContext(), "Looks like this movie doesn't have any trailers yet!", Toast.LENGTH_LONG).show();
    }

    private void showTrailers() {
        AsyncHttpClient client = new AsyncHttpClient();
        String requestUrl = String.format(SINGLE_MOVIE_TRAILER_URL, String.valueOf(movieID));
        client.get(requestUrl, new TextHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseBody) {
                final Trailer[] TRAILER_LIST = getTrailerListFromResponse(responseBody);
                fillAdapterWithNewData(TRAILER_LIST);

                trailersView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String key = TRAILER_LIST[position].getKey();
                        showTrailerWithIntent(key);
                    }
                });
            }

            private void showTrailerWithIntent(String key) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(YOUTUBE_BASE_URL + key));
                System.out.println("intent = " + YOUTUBE_BASE_URL + key);
                Toast.makeText(getApplicationContext(), "Movie key: " + key, Toast.LENGTH_LONG).show();
                startActivity(intent);
            }

            private Trailer[] getTrailerListFromResponse(String responseBody) {
                try {
                    JSONObject parsedResponse = new JSONObject(responseBody);
                    return JsonConverterFacade.convertResultsIntoTrailers(parsedResponse);
                } catch (JSONException e) {
                    e.printStackTrace();
                    return new Trailer[0];
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("Failed: ", "" + statusCode);
                Log.d("Error : ", "" + throwable);
            }

        });
    }

    private void fillAdapterWithNewData(Trailer[] trailerList) {
        trailerAdapter.clear();
        for (Trailer trailer : trailerList) {
            if (trailer != null) {
                Log.v(TAG, "POST EXECUTE URLS" + trailer);
                trailerAdapter.add(trailer);
            }
        }
        trailersView.setAdapter(trailerAdapter);
        trailerAdapter.notifyDataSetChanged();
    }
}