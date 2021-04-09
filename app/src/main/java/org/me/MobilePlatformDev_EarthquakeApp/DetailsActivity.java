/*
==========================================================================================================================================================================================================================================================
    Mobile Platform Development
    Ruari McGhee
    S1432402
==========================================================================================================================================================================================================================================================
*/

package org.me.MobilePlatformDev_EarthquakeApp;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.time.format.DateTimeFormatter;

public class DetailsActivity extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback
{
    EarthquakeInfo theInfo;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Log.e("MyTag","onCreate: Details Activity");

        // More Code goes here
        theInfo = (EarthquakeInfo) getIntent().getSerializableExtra("EarthquakeInfo");
        //Log.e("MyTag","onCreate: Info found for - " + earthquakeInfo.title);

        // Getting specific ui elements
        TextView locationTextView = (TextView) findViewById(R.id.earthquakeLocation);
        TextView dateTextView = (TextView) findViewById(R.id.earthquakeDate);
        TextView latitudeTextView = (TextView) findViewById(R.id.earthquakeLat);
        TextView longitudeTextView = (TextView) findViewById(R.id.earthquakeLong);
        TextView depthTextView = (TextView) findViewById(R.id.earthquakeDepth);
        TextView linkTextView = (TextView) findViewById(R.id.earthquakeLink);

        // Showing requested earthquake details
        String location = theInfo.descriptionElements.get("Location");
        location = location.replace(",", ", ");
        locationTextView.setText(location);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MM yy");
        String dateTime = theInfo.date.toString();
        dateTextView.setText(dateTime);

        String latitude = theInfo.latitude.toString();
        latitudeTextView.setText(latitude);

        String longitude = theInfo.longitude.toString();
        longitudeTextView.setText(longitude);

        String depth = theInfo.descriptionElements.get("Depth").toString();
        depthTextView.setText(depth);

        String link = theInfo.link;
        linkTextView.setText(link);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    public void onClick(View aview)
    {
        Log.e("MyTag","onClick: Details Activity");
    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        LatLng glasgow = new LatLng(theInfo.latitude,theInfo.longitude);

        Marker marker = googleMap.addMarker(new MarkerOptions().position(glasgow).title("Earthquake Marker"));
        marker.showInfoWindow();
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(glasgow, 7.5f));

        googleMap.getUiSettings().setMapToolbarEnabled(true);
        googleMap.getUiSettings().setAllGesturesEnabled(false);
    }
}