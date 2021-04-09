/*
==========================================================================================================================================================================================================================================================
    Mobile Platform Development
    Ruari McGhee
    S1432402
==========================================================================================================================================================================================================================================================
*/

package org.me.MobilePlatformDev_EarthquakeApp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.AsyncTask;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapLoadingTask extends AsyncTask<Void, Void, Void>
{
    private MapActivity mActivity = null;
    private GoogleMap theMap = null;

    private LatLngBounds.Builder mapBuilder = null;
    private ArrayList<MarkerOptions> markerOptions = null;
    private ArrayList<Bitmap> markerIcons = null;

    public MapLoadingTask(Context context, GoogleMap map)
    {
        super();
        mActivity = (MapActivity)context;
        theMap = map;
    }


    /*
    ========================================================================================================================================================================================================
    Asynctask Inherited Methods
    ========================================================================================================================================================================================================
    */
    @Override
    protected void onPreExecute()
    {

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected Void doInBackground(Void... params)
    {
        // Calculating the bounds of all of the parsed earthquake info
        mapBuilder = new LatLngBounds.Builder();
        markerOptions = new ArrayList<>();
        markerIcons = new ArrayList<>();

        // Creating marker for every parsed earthquake info
        ArrayList<EarthquakeInfo> earthquakeInfos = EarthquakeDataManager.Instance().GetEarthquakeInfos();
        for (EarthquakeInfo info: earthquakeInfos)
        {
            LatLng location = new LatLng(info.latitude,info.longitude);
            mapBuilder.include(location);

            markerOptions.add(new MarkerOptions().position(location).title(info.descriptionElements.get("Location")));

            LayerDrawable pinDrawable = (LayerDrawable)mActivity.getResources().getDrawable(R.drawable.pin_drawable);
            pinDrawable.findDrawableByLayerId(R.id.pin_background).setColorFilter(info.GetStrengthColor(), PorterDuff.Mode.MULTIPLY);

            int width = pinDrawable.getIntrinsicWidth();
            int height = pinDrawable.getIntrinsicHeight();

            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            pinDrawable.setBounds(0, 0, width, height);
            pinDrawable.draw(new Canvas(bitmap));

            markerIcons.add(Bitmap.createScaledBitmap(bitmap, 100, 150, false));
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result)
    {
        mActivity.ChangeState(MapActivity.MapActivityState.STATE_SHOWMAP);

        // Creating marker for every parsed earthquake info
        ArrayList<EarthquakeInfo> earthquakeInfos = EarthquakeDataManager.Instance().GetEarthquakeInfos();
        for (int i = 0; i < earthquakeInfos.size(); i++)
        {
            Marker newMarker = theMap.addMarker(markerOptions.get(i));
            newMarker.setTag(earthquakeInfos.get(i));
            newMarker.setIcon(BitmapDescriptorFactory.fromBitmap(markerIcons.get(i)));
        }

        // Moving map camera based on the bounds of all of the earthquake locations
        theMap.moveCamera(CameraUpdateFactory.newLatLngBounds(mapBuilder.build(), mActivity.activityViewFlipper.getWidth(), mActivity.activityViewFlipper.getHeight(), 50));

        // Preventing the user from moving the map
        theMap.getUiSettings().setMapToolbarEnabled(false);
        theMap.getUiSettings().setAllGesturesEnabled(true);

        // Customizing the marker click
        mActivity.detailsView.setOnClickListener(mActivity);
        theMap.setOnMapClickListener(mActivity);
        theMap.setOnMarkerClickListener(mActivity);
    }

    @Override
    protected void onProgressUpdate(Void... text)
    {

    }
}