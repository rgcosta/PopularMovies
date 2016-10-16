package com.example.android.popularmoviesst1;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class MoviesFragment extends Fragment {

    private ImageAdapter moviesAdapter;

    public MoviesFragment() {
    }

    @Override
    public void onStart() {
        super.onStart();
        FetchMoviesTask fetchMoviesTask = new FetchMoviesTask();
        fetchMoviesTask.execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main, container, false);

        //Define the adapter for the gridView
        moviesAdapter = new ImageAdapter(getActivity());

        GridView gridMovies = (GridView) view.findViewById(R.id.gridMovies);
        gridMovies.setAdapter(moviesAdapter);

        gridMovies.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                  Toast.makeText(getContext(), "" + position,
                      Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    public class FetchMoviesTask extends AsyncTask<Void, Void, ArrayList<Movie>> {

        private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();

        @Override
        protected ArrayList<Movie> doInBackground(Void... params) {

            /* If there's no sort order type, there's nothing to look up.  Verify size of params.
            if (params.length == 0){
                return null;
            }
            */

            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String MoviesJsonStr = null;

            try {
                // Construct the URL for the MoviesAPI query
                final String MOVIES_BASE_URI = "http://api.themoviedb.org/3/movie";
                final String APIKEY_PARAM = "api_key";

                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                String Order = sharedPreferences.getString(
                        getString(R.string.order_key),
                        getString(R.string.pref_sort_value_default));

                Uri builtUri = Uri.parse(MOVIES_BASE_URI).buildUpon()
                        .appendPath(Order)
                        .appendQueryParameter(APIKEY_PARAM, BuildConfig.MOVIES_API_KEY)
                        .build();

                //Working well!
                Log.v(LOG_TAG, builtUri.toString());

                URL url = new URL(builtUri.toString());

                //Create the request to MoviesAPI and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                //Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null){
                    //NOthing to do
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null){
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0){
                    //Stream was empty.
                    MoviesJsonStr = null;
                }
                MoviesJsonStr = buffer.toString();

                //TESTE OK
                Log.v(LOG_TAG, MoviesJsonStr);

            } catch (IOException e) {
                Log.e(LOG_TAG, "ERROR ", e);
                MoviesJsonStr = null;
            } finally {
                if (urlConnection != null){
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stram", e);
                    }
                }
            }

            try {
                return getMoviesDataFromJson(MoviesJsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();

            }

            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<Movie> result) {
            //super.onPostExecute(strings);
            if ( result != null){
                moviesAdapter.clearAll();
                moviesAdapter.addAll(result);
            }
        }

        private ArrayList<Movie> getMoviesDataFromJson(String MoviesJsonStr) throws JSONException {

            // These are the names of the JSON objects that need to be extracted.
            final String OWM_RESULTS = "results";
            final String OWN_POSTER_PATH = "poster_path";
            final String OWN_ID = "id";
            final String OWN_ORIGINAL_TITLE = "original_title";
            final String OWN_SYNOPSIS = "overview";
            final String OWN_VOTE_AVERAGE = "vote_average";
            final String OWN_RELEASE_DATE = "release_date";

            final String BASE_LINK = "http://image.tmdb.org/t/p/";
            final String POSTER_SIZE = Movie.posterSize;

            JSONObject moviesJson = new JSONObject(MoviesJsonStr);
            JSONArray moviesArray = moviesJson.getJSONArray(OWM_RESULTS);

            Movie movies;
            ArrayList result = new ArrayList<>();
            for (int i=0; i < moviesArray.length(); i++){

                //Get the JSON object representing the movie
                JSONObject movieObj = moviesArray.getJSONObject(i);

                //Get the movie's infos
                long id = movieObj.getLong(OWN_ID);
                String posterPath = movieObj.getString(OWN_POSTER_PATH);
                String originalTitle = movieObj.getString(OWN_ORIGINAL_TITLE);
                String Synopsis = movieObj.getString(OWN_SYNOPSIS);
                double voteAverage = movieObj.getDouble(OWN_VOTE_AVERAGE);
                String releaseDate = movieObj.getString(OWN_RELEASE_DATE);

                Uri Link = Uri.parse(BASE_LINK).buildUpon()
                        .appendPath(POSTER_SIZE)
                        .appendEncodedPath(posterPath)
                        .build();

                movies = new Movie(id, Link.toString(), originalTitle, Synopsis, voteAverage, releaseDate);
                result.add(movies);

                Log.v(LOG_TAG, "movieLink" + Link.toString());
            }

            return result;
        }


    }
}
