package pl.nataliana.popularmovies.adapters;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import pl.nataliana.popularmovies.R;
import pl.nataliana.popularmovies.TrailerActivity;
import pl.nataliana.popularmovies.model.Trailer;

/**
 * Created by Natalia Nazaruk on 28.02.2018.
 */

public class TrailerAdapter extends ArrayAdapter<Trailer> {

    private ArrayList<Trailer> trailers;
    private Context context;
    public static TextView linkTextView;

    public TrailerAdapter(Context context, ArrayList<Trailer> trailerList) {
        super(context, 0, trailerList);
        trailers = trailerList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return trailers.size();
    }

    @Override
    public Trailer getItem(int position) {
        return trailers.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View output = convertView;
        if (output == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            output = inflater.inflate(R.layout.trailer_list_item, parent, false);
        }

        linkTextView = output.findViewById(R.id.list_item_trailer_link);
        linkTextView.setText("https://www.youtube.com/watch?v=" + trailers.get(position).getKey());
        return output;
    }


}