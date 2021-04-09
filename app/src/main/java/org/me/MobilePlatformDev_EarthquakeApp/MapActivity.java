/*
==========================================================================================================================================================================================================================================================
    Mobile Platform Development
    Ruari McGhee
    S1432402
==========================================================================================================================================================================================================================================================
*/

package org.me.MobilePlatformDev_EarthquakeApp;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.time.format.DateTimeFormatter;

public class MapActivity extends AppCompatActivity implements GoogleMap.OnMarkerClickListener, GoogleMap.OnMapClickListener, OnMapReadyCallback, View.OnClickListener
{
    public SupportMapFragment mapFragment = null;
    public ViewFlipper activityViewFlipper = null;
    private AnimationDrawable loadingIcon = null;
    public View detailsView = null;

    private Marker lastClickedMarker = null;

    private MapActivity.MapActivityState currentState = MapActivity.MapActivityState.STATE_LOADING;
    public enum MapActivityState
    {
        STATE_LOADING,
        STATE_SHOWMAP
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        Log.e("MyTag","onCreate: Map Activity");

        detailsView = findViewById(R.id.earthquake_item);
        detailsView.animate().translationY(detailsView.getHeight());
        detailsView.setVisibility(View.INVISIBLE);

        // Obtain the SupportMapFragment
        // map will be notified when the map is ready to be used by the xml map parsing task.
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        activityViewFlipper = (ViewFlipper)findViewById(R.id.activityViewFlipper);
        ImageView loadingView = (ImageView)findViewById(R.id.loadingIcon);
        loadingView.setBackgroundResource(R.drawable.loading_drawable);
        loadingIcon = (AnimationDrawable)loadingView.getBackground();
        loadingIcon.start();

        // More Code goes here
        new XmlMapParsingTask(this).execute();
    }

    public void onClick(View view)
    {
        Log.e("MyTag","onClick: Map Activity - More Details Clicked");

        // Getting the next page to move to (Earthquake List Page)
        EarthquakeInfo info = (EarthquakeInfo) lastClickedMarker.getTag();
        Intent newActivity = new Intent(view.getContext(), DetailsActivity.class);

        // Passing the selected earthquake info to the next activity
        newActivity.putExtra("EarthquakeInfo", info);

        // Setting up activity switch animation
        Pair<View, String> p1 = Pair.create((View)this.findViewById(R.id.headerTitle), "TitleStartAnim");
        Pair<View, String> p2 = Pair.create((View)this.findViewById(R.id.headerSubtitle), "SubtitleStartAnim");
        Pair<View, String> p3 = Pair.create((View)this.findViewById(R.id.appIcon), "IconStartAnim");

        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(MapActivity.this, p1, p2, p3);

        // Switching activities
        startActivity(newActivity, options.toBundle());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public boolean onMarkerClick(Marker aMarker)
    {
        Log.e("MyTag","onMarkerClick: Map Activity");
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
        String dateTime = info.date.toString();
        dateTextView.setText(dateTime);

        // Earthquake Strength (Color)
        LayerDrawable icon = (LayerDrawable) strengthImageView.getDrawable();
        icon.findDrawableByLayerId(R.id.icon_background).setColorFilter(info.GetStrengthColor(), PorterDuff.Mode.MULTIPLY);
        strengthTextView.getBackground().setColorFilter(info.GetStrengthColor(), PorterDuff.Mode.MULTIPLY);

        // Earthquake Strength (Text)
        strengthTextView.setText(String.valueOf(info.GetStrengthValue()));

        // Animating the bottom details view to "open"
        detailsView.setVisibility(View.VISIBLE);

        // Returns false to allow the click event to continue and allow any default behaviour to occur
        return false;
    }

    @Override
    public void onMapClick(LatLng point)
    {
        Log.e("MyTag","onMapClick: Map Activity");
        lastClickedMarker = null;
        detailsView.setVisibility(View.INVISIBLE);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        new MapLoadingTask(this, googleMap).execute();
    }

    public MapActivity.MapActivityState GetCurrentState()
    {
        return currentState;
    }

    public void ChangeState(MapActivity.MapActivityState newState)
    {
        if (currentState == newState)
        {
            return;
        }

        currentState = newState;
        switch (currentState)
        {
            case STATE_LOADING:
            {
                activityViewFlipper.setDisplayedChild(0);
            }
            break;

            case STATE_SHOWMAP:
            {
                activityViewFlipper.setDisplayedChild(1);
            }
            break;
        }
    }
}
