/*
==========================================================================================================================================================================================================================================================
    Mobile Platform Development
    Ruari McGhee
    S1432402
==========================================================================================================================================================================================================================================================
*/

package org.me.MobilePlatformDev_EarthquakeApp;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

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

public class EarthquakeDataManager
{
    private ArrayList<EarthquakeInfo> theInfo = null;

    public LocalDateTime infoTime = null;

    /*
    ========================================================================================================================================================================================================
    Singleton Design Pattern
    ========================================================================================================================================================================================================
    */
    private static EarthquakeDataManager instance = null;
    public static EarthquakeDataManager Instance()
    {
        if (instance == null)
        {
            instance = new EarthquakeDataManager();
        }

        return instance;
    }


    /*
    ========================================================================================================================================================================================================
    Data Setters and Getters
    ========================================================================================================================================================================================================
    */
    public void SetEarthquakeInfos(ArrayList<EarthquakeInfo> newInfo)
    {
        theInfo = newInfo;
    }

    public ArrayList<EarthquakeInfo> GetEarthquakeInfos()
    {
        return theInfo;
    }

    public EarthquakeInfo GetEarthquakeInfo(Integer idx)
    {
        return theInfo.get(idx);
    }


    /*
    ========================================================================================================================================================================================================
    Downloading
    ========================================================================================================================================================================================================
    */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public String DownloadXml(String url)
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

        return result;
    }


    /*
    ========================================================================================================================================================================================================
    Parsing
    ========================================================================================================================================================================================================
    */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public ArrayList<EarthquakeInfo> ParseXml(String readXmlData) throws XmlPullParserException, IOException
    {
        ArrayList<EarthquakeInfo> parsedEarthquakeInfo = new ArrayList<EarthquakeInfo>();

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
        return parsedEarthquakeInfo;
    }
}