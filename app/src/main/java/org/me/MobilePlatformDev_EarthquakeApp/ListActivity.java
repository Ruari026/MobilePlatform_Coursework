package org.me.MobilePlatformDev_EarthquakeApp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.usage.UsageEvents;
import android.os.Build;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

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

        new XMLParsingTask(this).execute();
    }

    public void onClick(View aview)
    {
        Log.e("MyTag","onClick: List Activity");
    }
}
