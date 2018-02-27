package pl.nataliana.popularmovies;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import pl.nataliana.popularmovies.adapters.ReviewAdapter;
import pl.nataliana.popularmovies.model.Movie;
import pl.nataliana.popularmovies.model.Review;

/**
 * Created by Natalia Nazaruk on 25.02.2018.
 */

public class ReviewActivity extends Activity {

    public static final String API_KEY = BuildConfig.API_KEY; //Please add your proper API key to project:gradle.properties file
    private static final String TAG = ReviewActivity.class.getSimpleName();
    public static final String SINGLE_MOVIE_REVIEWS_URL = "https://api.themoviedb.org/3/movie/%s/reviews?api_key=" + API_KEY;
    private ReviewAdapter reviewAdapter;
    public ListView reviewsView;
    public long movieID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        reviewAdapter = new ReviewAdapter(this, new ArrayList<Review>());
        reviewsView = findViewById(R.id.list);

        movieID = getIntent().getExtras().getLong(getString(R.string.movie_id));

        if (savedInstanceState == null) {
            showReviews();
        } else {
            Parcelable[] parcelable = savedInstanceState.
                    getParcelableArray(getString(R.string.review_parcelable));

            if (parcelable != null) {
                int numReviewObjects = parcelable.length;
                Review[] reviews = new Review [numReviewObjects];
                for (int i = 0; i < numReviewObjects; i++) {
                    reviews[i] = (Review) parcelable[i];
                }

                // Load review objects into view
                reviewsView.setAdapter(reviewAdapter);
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        int numReviewObjects = reviewsView.getCount();
        if (numReviewObjects > 0) {
            Review[] reviews = new Review[numReviewObjects];
            for (int i = 0; i < numReviewObjects; i++) {
                reviews[i] = (Review) reviewsView.getItemAtPosition(i);
            }

            // Save Review objects to bundle
            outState.putParcelableArray(getString(R.string.review_parcelable), reviews);
        }

        super.onSaveInstanceState(outState);
    }

    private void showReviews() {
        AsyncHttpClient client = new AsyncHttpClient();
        String requestUrl = String.format(SINGLE_MOVIE_REVIEWS_URL,String.valueOf(movieID) , API_KEY);
        client.get(requestUrl, new TextHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseBody) {
                try {
                    JSONObject jsonObj = new JSONObject(responseBody);
                    JSONArray reviews = jsonObj.getJSONArray("results");

                    // looping through reviews
                    Review[] reviewList = new Review[5];
                    for (int i = 0; i < reviews.length(); ++i) {
                        JSONObject review = reviews.getJSONObject(i);
                        reviewList[i] = new Review(
                                review.getString("id"),
                                review.getString("author"),
                                review.getString("content"));

                    }
                    reviewAdapter.clear();
                    for (Review review : reviewList) {
                        if (review != null) {
                            Log.v(TAG, "POST EXECUTE URLS" + review);
                            reviewAdapter.add(review);
                        }
                    }
                    reviewAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    Log.e(TAG, "Error", e);
                }
                reviewsView.setAdapter(reviewAdapter);
                reviewAdapter.notifyDataSetChanged();
            }


            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("Failed: ", "" + statusCode);
                Log.d("Error : ", "" + throwable);
            }

        });
    }
}