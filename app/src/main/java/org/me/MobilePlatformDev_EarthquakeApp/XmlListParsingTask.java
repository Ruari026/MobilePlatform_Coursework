package org.me.MobilePlatformDev_EarthquakeApp;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class XmlListParsingTask extends AsyncTask<Void, Void, Void>
{
    private String urlSource="http://quakes.bgs.ac.uk/feeds/MhSeismology.xml";
    private ArrayList<EarthquakeInfo> parsedInfo = null;
    private ListActivity mActivity = null;


    public XmlListParsingTask(Context context)
    {
        super();
        mActivity = (ListActivity)context;
    }


    /*
    ========================================================================================================================================================================================================
    Asynctask Inherited Methods
    ========================================================================================================================================================================================================
    */
    @Override
    protected void onPreExecute()
    {
        super.onPreExecute();
        Log.e("MyTag", "A Sync Task Started: XML Parsing for List View");

        // Start Loading Icon
    }

    @Override
    @RequiresApi(api = Build.VERSION_CODES.O)
    protected Void doInBackground(Void... params)
    {
        // Checking when the data was last downloaded
        LocalDateTime lastParseTime = EarthquakeDataManager.Instance().infoTime;
        LocalDateTime currentTime = LocalDateTime.now();
        if (lastParseTime == null)
        {
            // Download xml file from internet
            String result = "";
            result = EarthquakeDataManager.Instance().DownloadXml(urlSource);

            // Parse information from xml
            //Log.i("MyTag","XML Read: "+  result.toString());
            parsedInfo = null;
            try
            {
                parsedInfo = EarthquakeDataManager.Instance().ParseXml(result);
            }
            catch (Exception e)
            {
                Log.e("MyTag", "Exception thrown during parsing: " + e.getMessage());
            }

            // Saving parsed info
            EarthquakeDataManager.Instance().SetEarthquakeInfos(parsedInfo);
            EarthquakeDataManager.Instance().infoTime = LocalDateTime.now();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void result)
    {
        super.onPostExecute(result);
        Log.e("MyTag", "A Sync Task Finished: XML Parsing");

        // Hide Loading Icon

        // Create List of interactable fragments
        EarthquakeListFragment fragment = new EarthquakeListFragment(parsedInfo);

        FragmentManager manager = mActivity.getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fragmentList, fragment);
        transaction.commit();
    }
}