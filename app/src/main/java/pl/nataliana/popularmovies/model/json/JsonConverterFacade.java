package pl.nataliana.popularmovies.model.json;

import org.json.JSONException;
import org.json.JSONObject;

import pl.nataliana.popularmovies.model.Trailer;

/**
 * Created by Natalia Nazaruk on 01.03.2018.
 */

public class JsonConverterFacade {
    public static Trailer[] convertResultsIntoTrailers(JSONObject json){
        try {
            return TrailerConverter.getTrailerList(json);
        } catch (JSONException e) {
            e.printStackTrace();
            return new Trailer[0];
        }
    }
}
