package pl.nataliana.popularmovies.model.json;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import pl.nataliana.popularmovies.model.Trailer;

/**
 * Created by Natalia Nazaruk on 01.03.2018.
 */

class TrailerConverter {
    private static final String ARRAY_WITH_RESULTS_NAME = "results";
    private static final String TRAILER_ID_NAME = "id";
    private static final String TRAILER_KEY_NAME = "key";

    static Trailer[] getTrailerList(JSONObject json) throws JSONException {
        JSONArray array = json.getJSONArray(ARRAY_WITH_RESULTS_NAME);

        Trailer[] trailerList = new Trailer[array.length()];
        for (int i = 0; i < array.length(); ++i) {
            JSONObject trailer = array.getJSONObject(i);
            trailerList[i] = getTrailer(trailer);
        }
        return trailerList;
    }

    private static Trailer getTrailer(JSONObject trailer) {
        return new Trailer(
                getTrailerId(trailer),
                getTrailerKey(trailer));
    }

    private static String getTrailerKey(JSONObject trailer) {
        try {
            return trailer.getString(TRAILER_KEY_NAME);
        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }
    }

    private static String getTrailerId(JSONObject trailer) {
        try {
            return trailer.getString(TRAILER_ID_NAME);
        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }
    }
}
