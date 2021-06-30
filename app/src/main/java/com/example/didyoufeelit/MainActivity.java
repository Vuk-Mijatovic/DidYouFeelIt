package com.example.didyoufeelit;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    /** URL for earthquake data from the USGS dataset */
    private static final String USGS_REQUEST_URL =
            "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&starttime=2016-01-01&endtime=2016-05-02&minfelt=50&minmagnitude=5";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DidYouFeelItAsyncTask task = new DidYouFeelItAsyncTask();
        task.execute(USGS_REQUEST_URL);
    }

    /**
     * Update the UI with the given earthquake information.
     */
    private void updateUi(Event earthquake) {
        TextView titleTextView = (TextView) findViewById(R.id.title);
        titleTextView.setText(earthquake.title);

        TextView tsunamiTextView = (TextView) findViewById(R.id.number_of_people);
        tsunamiTextView.setText(getString(R.string.num_people_felt_it, earthquake.numOfPeople));

        TextView magnitudeTextView = (TextView) findViewById(R.id.perceived_magnitude);
        magnitudeTextView.setText(earthquake.perceivedStrength);
    }

    private class DidYouFeelItAsyncTask extends AsyncTask <String, Void, Event>{

        @Override
        protected Event doInBackground(String... requestUrls) {
            // Dont perform action if there is no urls or the first one is null
            if (requestUrls.length < 1 || requestUrls[0] == null){
                return null;
            }
            // Perform the HTTP request for earthquake data and process the response.
            Event earthquake = Utils.fetchEarthquakeData(requestUrls[0]);
             return earthquake;
        }

        @Override
        protected void onPostExecute(Event earthquake) {
            //Don`t update if earthquake is null
            if (earthquake == null){
                return;
            }
            // Update the information displayed to the user.
            updateUi(earthquake);
        }
    }
}