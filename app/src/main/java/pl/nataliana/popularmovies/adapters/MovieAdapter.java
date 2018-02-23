package pl.nataliana.popularmovies.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import pl.nataliana.popularmovies.R;
import pl.nataliana.popularmovies.MainActivity;
import pl.nataliana.popularmovies.model.Movie;

/**
 * Created by Natalia Nazaruk on 20.02.2018.
 */

public class MovieAdapter extends ArrayAdapter<Movie> {

    public ArrayList<Movie> movies;
    private Context context;

    public MovieAdapter(Context context, ArrayList<Movie> movieList) {
        super(context, 0, movieList);
        movies = new ArrayList<>();
        if (movieList != null) {
            movies = movieList;
        }
        this.context = context;
    }

    @Override
    public int getCount() {
        return movies.size();
    }

    @Override
    public Movie getItem(int position) {
        return movies.get(position);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View output = convertView;
        if (output == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            output = inflater.inflate(R.layout.movie_grid_item, parent, false);
        }
        ImageView posterImageView = output.findViewById(R.id.movie_item_iv);

        Movie movie = getItem(position);
        String url = MainActivity.MOVIE_POSTER_BASE_URL + MainActivity.POSTER_SIZE + movie.getPoster();
        Picasso.with(context).load(url).placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder).into(posterImageView);
        return output;
    }
}