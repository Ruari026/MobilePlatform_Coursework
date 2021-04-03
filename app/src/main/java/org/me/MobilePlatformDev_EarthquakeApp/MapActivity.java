package org.me.MobilePlatformDev_EarthquakeApp;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowInsetsAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;

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
    private TextView titleText = null;
    private TextView subtitleText = null;
    private ImageView appIcon = null;

    public SupportMapFragment mapFragment = null;
    private View detailsView = null;
    private Marker lastClickedMarker = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        Log.e("MyTag","onCreate: Map Activity");

        titleText = (TextView)findViewById(R.id.salutation);
        subtitleText = (TextView)findViewById(R.id.acknowledgement);
        appIcon = (ImageView)findViewById(R.id.appIcon);

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

        // Setting up activity switch animation
        Pair<View, String> p1 = Pair.create((View)titleText, "TitleStartAnim");
        Pair<View, String> p2 = Pair.create((View)subtitleText, "SubtitleStartAnim");
        Pair<View, String> p3 = Pair.create((View)appIcon, "IconStartAnim");

        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(MapActivity.this, p1, p2, p3);

        // Switching activities
        startActivity(newActivity, options.toBundle());
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
        ImageView strengthImageView = (ImageView) detailsView.findViewById(R.id.earthquakeStrengthIcon);
        TextView strengthTextView = (TextView) detailsView.findViewById(R.id.earthquakeStrengthValue);

        // Populate the data into the template view using the data object
        // Earthquake Location
        String location = info.descriptionElements.get("Location");
        location = location.replace(",", ", ");
        locationTextView.setText(location);

        // Earthquake Date & Time
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MM yy");
        String dateTime = info.date.format(formatter).replace(" ", "-");
        dateTextView.setText(dateTime);

        // Earthquake Strength (Color)
        float strength = Float.parseFloat(info.descriptionElements.get("Magnitude"));
        int color = Color.BLACK;
        if (strength >= 0.0f && strength < 2.5f)
        {
            float lerp = (strength - 0.0f) / 2.5f;
            color = Utility.ColorLerp(Color.BLUE, Color.GREEN, lerp);
        }
        else if (strength >= 2.5f && strength < 5.0f)
        {
            float lerp = (strength - 2.5f) / 2.5f;
            color = Utility.ColorLerp(Color.GREEN, Color.YELLOW, lerp);
        }
        else if (strength >= 5.0f && strength < 7.5f)
        {
            float lerp = (strength - 5.0f) / 2.5f;
            color = Utility.ColorLerp(Color.YELLOW, Color.parseColor("#FFA500"), lerp);
        }
        else if (strength >= 7.5f && strength < 10.0f)
        {
            float lerp = (strength - 7.5f) / 2.5f;
            color = Utility.ColorLerp(Color.parseColor("#FFA500"), Color.RED, lerp);
        }
        LayerDrawable icon = (LayerDrawable) strengthImageView.getDrawable();
        icon.findDrawableByLayerId(R.id.icon_background).setColorFilter(color, PorterDuff.Mode.MULTIPLY);
        strengthTextView.getBackground().setColorFilter(color, PorterDuff.Mode.MULTIPLY);

        // Earthquake Strength (Text)
        strengthTextView.setText(info.descriptionElements.get("Magnitude"));

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
