package org.me.MobilePlatformDev_EarthquakeApp;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.time.format.DateTimeFormatter;

public class DetailsActivity extends AppCompatActivity implements View.OnClickListener
{
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Log.e("MyTag","onCreate: Details Activity");

        // More Code goes here
        EarthquakeInfo earthquakeInfo = (EarthquakeInfo) getIntent().getSerializableExtra("EarthquakeInfo");
        //Log.e("MyTag","onCreate: Info found for - " + earthquakeInfo.title);

        // Getting specific ui elements
        TextView locationTextView = (TextView) findViewById(R.id.earthquakeLocation);
        TextView dateTextView = (TextView) findViewById(R.id.earthquakeDate);
        TextView latitudeTextView = (TextView) findViewById(R.id.earthquakeLat);
        TextView longitudeTextView = (TextView) findViewById(R.id.earthquakeLong);
        TextView depthTextView = (TextView) findViewById(R.id.earthquakeDepth);
        TextView linkTextView = (TextView) findViewById(R.id.earthquakeLink);

        // Showing requested earthquake details
        String location = earthquakeInfo.descriptionElements.get("Location");
        location = location.replace(",", ", ");
        locationTextView.setText(location);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MM yy");
        String dateTime = earthquakeInfo.date.format(formatter).replace(" ", "-");
        dateTextView.setText(dateTime);

        String latitude = earthquakeInfo.latitude.toString();
        latitudeTextView.setText(latitude);

        String longitude = earthquakeInfo.longitude.toString();
        longitudeTextView.setText(longitude);

        String depth = earthquakeInfo.descriptionElements.get("Depth").toString();
        depthTextView.setText(depth);

        String link = earthquakeInfo.link;
        linkTextView.setText(link);
    }

    public void onClick(View aview)
    {
        Log.e("MyTag","onClick: Map Activity");
    }
}