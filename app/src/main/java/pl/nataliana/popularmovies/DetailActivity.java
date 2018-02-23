package pl.nataliana.popularmovies;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import pl.nataliana.popularmovies.R;
import pl.nataliana.popularmovies.model.Movie;

/**
 * Created by Natalia Nazaruk on 17.02.2018.
 */

public class DetailActivity extends Activity {

    private final String TAG = DetailActivity.class.getSimpleName();


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ImageView ivPoster = (ImageView) findViewById(R.id.poster_image_iv);
        TextView tvOriginalTitle = (TextView) findViewById(R.id.original_title_tv);
        TextView tvOverView = (TextView) findViewById(R.id.synopsis_text_tv);
        TextView tvVoteAverage = (TextView) findViewById(R.id.rating_value_tv);
        TextView tvReleaseDate = (TextView) findViewById(R.id.date_value_tv);

        Intent intent = getIntent();
        Movie movie = intent.getParcelableExtra(getString(R.string.movie_parcelable));

        Picasso.with(this)
                .load(movie.getPoster())
                .error(R.drawable.placeholder)
                .placeholder(R.drawable.placeholder)
                .into(ivPoster);

        tvOriginalTitle.setText(movie.getTitle());
        tvOverView.setText(movie.getSynopsis());
        tvVoteAverage.setText(movie.getRating());
        tvReleaseDate.setText(movie.getDate());


    }
}

