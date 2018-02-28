package pl.nataliana.popularmovies.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import pl.nataliana.popularmovies.R;
import pl.nataliana.popularmovies.model.Review;

/**
 * Created by Natalia Nazaruk on 25.02.2018.
 */

public class ReviewAdapter extends ArrayAdapter<Review> {

    private ArrayList<Review> reviews;
    private Context context;

    public ReviewAdapter(Context context, ArrayList<Review> reviewList) {
        super(context, 0, reviewList);
        reviews = reviewList;
        if (reviewList != null) {
            reviews = reviewList;
        }
        this.context = context;
    }

    @Override
    public int getCount() {
        return reviews.size();
    }

    @Override
    public Review getItem(int position) {
        return reviews.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View output = convertView;
        if (output == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            output = inflater.inflate(R.layout.review_list_item, parent, false);
        }

        TextView authorTextView = output.findViewById(R.id.list_item_review_author);
        TextView reviewTextView = output.findViewById(R.id.list_item_review_text);
        authorTextView.setText(reviews.get(position).getAuthor());
        reviewTextView.setText(reviews.get(position).getReview());

        return output;
    }
}
