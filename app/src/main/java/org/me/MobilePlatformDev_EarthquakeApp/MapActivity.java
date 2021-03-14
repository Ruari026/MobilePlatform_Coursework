package org.me.MobilePlatformDev_EarthquakeApp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MapActivity extends AppCompatActivity implements View.OnClickListener
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        Log.e("MyTag","onCreate: Map Activity");

        // More Code goes here
    }

    public void onClick(View aview)
    {
        Log.e("MyTag","onClick: Map Activity");
    }
}
