package pl.nataliana.popularmovies;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

/**
 * Created by Natalia Nazaruk on 25.02.2018.
 */

public class ReviewActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.review_list_item);

        final ListView listview = findViewById(R.id.list);
        String[] values = new String[]{"Android", "iPhone", "WindowsMobile",
                "Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X",
                "Linux", "OS/2", "Ubuntu", "Windows7", "Max OS X", "Linux",
                "OS/2", "Ubuntu", "Windows7", "Max OS X", "Linux", "OS/2",
                "Android", "iPhone", "WindowsMobile"};
    }
}