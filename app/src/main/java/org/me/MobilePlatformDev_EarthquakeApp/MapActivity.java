package org.me.MobilePlatformDev_EarthquakeApp;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowInsetsAnimation;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class MapActivity extends AppCompatActivity implements GoogleMap.OnMarkerClickListener, OnMapReadyCallback, View.OnClickListener
{
    public SupportMapFragment mapFragment = null;
    private View detailsView = null;
    private Marker lastClickedMarker = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        Log.e("MyTag","onCreate: Map Activity");

        // Getting the details view at the bottom of the page
        detailsView = findViewById(R.id.earthquake_item);
        detailsView.findViewById(R.id.detailsButton).setOnClickListener(this);

        // Obtain the SupportMapFragment
        // map will be notified when the map is ready to be used by the xml map parsing task.
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        // More Code goes here
        new XmlMapParsingTask(this).execute();
    }

    public void onClick(View view)
    {
        Log.e("MyTag","onClick: Details Activity - Button Click");

        // Getting the next page to move to (Earthquake List Page)
        EarthquakeInfo info = (EarthquakeInfo) lastClickedMarker.getTag();
        Intent newActivity = new Intent(view.getContext(), DetailsActivity.class);

        // Passing the selected earthquake info to the next activity
        newActivity.putExtra("EarthquakeInfo", info);

        // Switching activities
        startActivity(newActivity);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public boolean onMarkerClick(Marker aMarker)
    {
        Log.e("MyTag","onClick: Details Activity - Marker Click");
        lastClickedMarker = aMarker;

        // Setting info on the details section based on the marker clicked
        EarthquakeInfo info = (EarthquakeInfo) aMarker.getTag();

        TextView locationTextView = (TextView) detailsView.findViewById(R.id.earthquakeLocation);
        TextView dateTextView = (TextView) detailsView.findViewById(R.id.earthquakeDate);
        TextView linkTextView = (TextView) detailsView.findViewById(R.id.earthquakeLink);
        View buttonView = detailsView.findViewById(R.id.earthquake_item);

        // Populate the data into the template view using the data object
        String location = info.descriptionElements.get("Location");
        location = location.replace(",", ", ");
        locationTextView.setText(location);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MM yy");
        String dateTime = info.date.format(formatter).replace(" ", "-");
        dateTextView.setText(dateTime);

        linkTextView.setText(info.link);

        // Animating the bottom details view to "open"

        // Returns false to allow the click event to continue and allow any default behaviour to occur
        return false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        // Calculating the bounds of all of the parsed earthquake info
        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        // Creating marker for every parsed earthquake info
        ArrayList<EarthquakeInfo> earthquakeInfos = EarthquakeDataManager.Instance().GetEarthquakeInfos();
        for (EarthquakeInfo info: earthquakeInfos)
        {
            LatLng location = new LatLng(info.latitude,info.longitude);
            builder.include(location);

            Marker newMarker = googleMap.addMarker(new MarkerOptions().position(location).title(info.descriptionElements.get("Location")));
            newMarker.setTag(info);
        }

        // Moving map camera based on the bounds of all of the earthquake locations
        googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 0));

        // Preventing the user from moving the map
        googleMap.getUiSettings().setMapToolbarEnabled(false);
        googleMap.getUiSettings().setAllGesturesEnabled(false);

        // Customizing the marker click
        googleMap.setOnMarkerClickListener(this);
    }
}
