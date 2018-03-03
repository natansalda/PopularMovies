package pl.nataliana.popularmovies;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import pl.nataliana.popularmovies.data.FavMovieContract;
import pl.nataliana.popularmovies.model.Movie;

/**
 * Created by Natalia Nazaruk on 17.02.2018.
 */

public class DetailActivity extends Activity {

    private Long movieID;
    private static final String TAG = DetailActivity.class.getSimpleName(); // For debugging purposes
    private Movie movie;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        final ImageView ivPoster = findViewById(R.id.poster_image_iv);
        final ImageView ivFav = findViewById(R.id.fav_iv);
        TextView tvOriginalTitle = findViewById(R.id.original_title_tv);
        TextView tvOverView = findViewById(R.id.synopsis_text_tv);
        TextView tvVoteAverage = findViewById(R.id.rating_value_tv);
        TextView tvReleaseDate = findViewById(R.id.date_value_tv);
        Button btReview = findViewById(R.id.review_button);
        Button btTrailer = findViewById(R.id.trailer_button);

        Intent intent = getIntent();
        movieID = getIntent().getExtras().getLong(getString(R.string.movie_id_extras));
        movie = intent.getParcelableExtra(getString(R.string.movie_parcelable));

        Picasso.with(this)
                .load(movie.getPosterUrl())
                .error(R.drawable.nowifi)
                .placeholder(R.drawable.placeholder)
                .into(ivPoster);

        tvOriginalTitle.setText(movie.getTitle());
        tvOverView.setText(movie.getSynopsis());
        tvVoteAverage.setText(movie.getRating());
        tvReleaseDate.setText(movie.getDate());
        setIconFavorites();

        ivFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean inFavorites = checkFavorites(movie.getId());
                if (inFavorites) {
                    deleteFromFavorites(movieID);
                } else {
                    addToFav(movieID);
                }
                setIconFavorites();
            }
        });

        btReview.setOnClickListener(new AdapterView.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailActivity.this, ReviewActivity.class);
                intent.putExtra(getString(R.string.movie_id), movieID);
                startActivity(intent);
            }
        });

        btTrailer.setOnClickListener(new AdapterView.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailActivity.this, TrailerActivity.class);
                intent.putExtra(getString(R.string.movie_id), movieID);
                startActivity(intent);
            }
        });
    }

    // Add movie to the database
    private void addToFav(Long movieID) {

        Uri uri = FavMovieContract.MovieEntry.CONTENT_URI;
        ContentResolver resolver = this.getContentResolver();
        ContentValues values = new ContentValues();
        values.clear();

        values.put(FavMovieContract.MovieEntry.MOVIE_ID, movieID);
        values.put(FavMovieContract.MovieEntry.MOVIE_TITLE, movie.getTitle());
        values.put(FavMovieContract.MovieEntry.MOVIE_OVERVIEW, movie.getSynopsis());
        values.put(FavMovieContract.MovieEntry.MOVIE_RELEASE_DATE, movie.getDate());
        values.put(FavMovieContract.MovieEntry.MOVIE_POSTER, movie.getPoster());
        values.put(FavMovieContract.MovieEntry.MOVIE_VOTE_AVERAGE, movie.getRating());
        values.put(FavMovieContract.MovieEntry.MOVIE_POPULARITY, movie.getPopularity());

        Uri check = resolver.insert(uri, values);
        Toast.makeText(getApplicationContext(), "Movie added to favorites!", Toast.LENGTH_LONG).show();
    }


    // Delete movie from the database
    private void deleteFromFavorites(Long movieID) {

        Uri uri = FavMovieContract.MovieEntry.CONTENT_URI;
        ContentResolver resolver = this.getContentResolver();

        long noDeleted = resolver.delete(uri,
                FavMovieContract.MovieEntry.MOVIE_ID + " = ? ",
                new String[]{movieID + ""});

        Toast.makeText(getApplicationContext(), "Movie removed favorites!", Toast.LENGTH_LONG).show();

    }


    // Check if the movie is already in the database
    private boolean checkFavorites(Long movieID) {

        Uri uri = FavMovieContract.MovieEntry.buildMovieUri(movieID);
        ContentResolver resolver = this.getContentResolver();
        Cursor cursor = null;

        try {

            cursor = resolver.query(uri, null, null, null, null);
            if (cursor.moveToFirst())
                return true;

        } finally {

            if (cursor != null)
                cursor.close();

        }

        return false;
    }

    // Set proper favorites icon
    private void setIconFavorites() {
        boolean inFavorites = checkFavorites(movie.getId());
        ImageView addToFav = findViewById(R.id.fav_iv);

        if (inFavorites) {
            addToFav.setImageResource(R.drawable.fav_full);
        } else {
            addToFav.setImageResource(R.drawable.fav);
        }
    }
}