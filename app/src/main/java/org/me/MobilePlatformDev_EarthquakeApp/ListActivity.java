package org.me.MobilePlatformDev_EarthquakeApp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.util.Log;
import android.view.View;

public class ListActivity extends AppCompatActivity implements View.OnClickListener
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Log.e("MyTag","onCreate: List Activity");

        // Set up the raw links to the graphical components
        //startButton = (Button)findViewById(R.id.startButton);
        //startButton.setOnClickListener(this);
        //Log.e("MyTag","after startButton");

        new XmlListParsingTask(this).execute();
    }

    public void onClick(View aview)
    {
        Log.e("MyTag","onClick: List Activity");
    }
}
