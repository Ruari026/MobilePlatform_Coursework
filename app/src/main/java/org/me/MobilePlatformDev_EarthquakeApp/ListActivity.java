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
    private Button startButton;
    private String urlSource="http://quakes.bgs.ac.uk/feeds/MhSeismology.xml";
    public static List<EarthquakeInfo> parsedEarthquakeInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Log.e("MyTag","onCreate: List Activity");

        // Set up the raw links to the graphical components
        //startButton = (Button)findViewById(R.id.startButton);
        //startButton.setOnClickListener(this);
        Log.e("MyTag","after startButton");

        startProgress();
    }

    public void onClick(View aview)
    {
        Log.e("MyTag","onClick: List Activity");
    }

    public void startProgress()
    {
        // Run network access on a separate thread;
        new Thread(new Task(urlSource)).start();
    } //

    // Need separate thread to access the internet resource over network
    // Other neater solutions should be adopted in later iterations.
    private class Task implements Runnable
    {
        private String url;

        public Task(String aurl)
        {
            url = aurl;
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void run()
        {
            URL aurl;
            URLConnection yc;
            BufferedReader in = null;
            String inputLine = "";
            String result = "";

            Log.e("MyTag","in run");

            try
            {
                Log.e("MyTag","in try");
                aurl = new URL(url);
                yc = aurl.openConnection();
                in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
                Log.e("MyTag","after ready");
                //
                // Throw away the first 2 header lines before parsing
                //
                //
                while ((inputLine = in.readLine()) != null)
                {
                    if (result == null)
                    {
                        result = inputLine;
                    }
                    else
                    {
                        result = result + inputLine;
                    }
                    //Log.e("MyTag",inputLine);
                }
                in.close();
            }
            catch (IOException ae)
            {
                Log.e("MyTag", "ioexception in run");
            }

            // Now that you have the xml data you can parse it
            //Log.i("MyTag","XML Read: "+  result.toString());
            try
            {
                Parse(result);
            }
            catch (Exception e)
            {
                Log.e("MyTag", "Exception thrown during parsing: " + e.getMessage());
            }

            // Now update the TextView to display raw XML data
            ListActivity.this.runOnUiThread(new Runnable()
            {
                public void run() {
                    Log.d("UI thread", "I am the UI thread");

                    EarthquakeListFragment fragment = new EarthquakeListFragment(parsedEarthquakeInfo);

                    FragmentManager manager = getSupportFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();
                    transaction.replace(R.id.fragmentList, fragment);
                    transaction.commit();
                }
            });
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        private void Parse(String readXmlData) throws XmlPullParserException, IOException
        {
            parsedEarthquakeInfo = new ArrayList<EarthquakeInfo>();

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();

            xpp.setInput( new StringReader(readXmlData) );
            int eventType= xpp.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT)
            {
                if(eventType == XmlPullParser.START_TAG)
                {
                    String name = xpp.getName();
                    //Log.e("MyTag", "Start tag -  "+ name);

                    if (name.equalsIgnoreCase("item"))
                    {
                        // Setup info to store in list
                        EarthquakeInfo newInfo = new EarthquakeInfo();

                        // Parsing the specific earthquake info
                        Boolean finished = false;
                        while (!finished)
                        {
                            if (eventType == XmlPullParser.START_TAG)
                            {
                                // Checking what piece of info has been found
                                String tagName = xpp.getName();
                                if (tagName.equalsIgnoreCase("Title"))
                                {
                                    String newTitle = xpp.nextText();
                                    newInfo.SetTitle(newTitle);
                                }
                                else if (tagName.equalsIgnoreCase("Description"))
                                {
                                    String newDescription = xpp.nextText();
                                    newInfo.SetDescription(newDescription);
                                }
                                else if (tagName.equalsIgnoreCase("Link"))
                                {
                                    String newLink = xpp.nextText();
                                    newInfo.SetLink(newLink);
                                }
                                else if (tagName.equalsIgnoreCase("PubDate"))
                                {
                                    String newPubDate = xpp.nextText();
                                    newInfo.SetPubDate(newPubDate);
                                }
                                else if (tagName.equalsIgnoreCase("Category"))
                                {
                                    String newCategory = xpp.nextText();
                                    newInfo.SetCategory(newCategory);
                                }
                                else if (tagName.equalsIgnoreCase("Lat"))
                                {
                                    String newLat = xpp.nextText();
                                    newInfo.SetLatitude(newLat);
                                }
                                else if (tagName.equalsIgnoreCase("Long"))
                                {
                                    String newLong = xpp.nextText();
                                    newInfo.SetLongitude(newLong);
                                }

                                // Get the next event
                                eventType = xpp.next();
                            }
                            else if (eventType == XmlPullParser.END_TAG)
                            {
                                String endTagName = xpp.getName();
                                if (endTagName.equalsIgnoreCase("item"))
                                {
                                    finished = true;
                                }
                            }
                        }

                        // Storing the info to use later
                        parsedEarthquakeInfo.add(newInfo);
                    }
                }

                // Get the next event
                eventType = xpp.next();
            }
            System.out.println("End document");
        }
    }
}
