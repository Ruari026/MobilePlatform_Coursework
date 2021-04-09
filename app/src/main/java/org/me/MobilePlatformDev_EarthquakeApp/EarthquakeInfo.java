/*
==========================================================================================================================================================================================================================================================
    Mobile Platform Development
    Ruari McGhee
    S1432402
==========================================================================================================================================================================================================================================================
*/

package org.me.MobilePlatformDev_EarthquakeApp;

import android.graphics.Color;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;

public class EarthquakeInfo implements Serializable
{
    public String title = "";

    public HashMap<String, String> descriptionElements = new HashMap<String, String>();

    public String link = "";

    public LocalDate date = null;
    public DayOfWeek day = null;
    public LocalTime time = null;

    public String category = "";

    public Float latitude = 0.0f;
    public Float longitude = 0.0f;

    /*
    ========================================================================================================================================================================================================
    Setters
    ========================================================================================================================================================================================================
    */
    /**
     *  Stores the earthquake title information (Normally contains basic info for: Magnitude, Location, Date, & Time)
     * @param titleData
     */
    public void SetTitle(String titleData)
    {
        title = titleData;
    }

    /**
     *  Handles splitting up and storing each key value pair in the description (Description should containv info for: Date, Time, Location, Latitude, Longitude, Depth, & Magnitude)
     * @param descriptionData
     */
    public void SetDescription(String descriptionData)
    {
        // getting more detailed information from the raw data
        String[] info = descriptionData.split(";");

        // Splitting each piece of info into a key value pair and storing
        descriptionElements = new HashMap<String, String>();
        for (int i = 0; i < info.length; i++)
        {
            // Stores all the elements in a HashMap for simple access
            String[] pair = info[i].split(":", 2);
            descriptionElements.put(pair[0].trim(), pair[1].trim());
        }
    }

    /**
     *  Stores the link to the earthquake data
     * @param linkData
     */
    public void SetLink(String linkData)
    {
        link = linkData;
    }

    /**
     *  Handles splitting up and storing the earthquake's date and time information
     * @param pubDateData
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void SetPubDate(String pubDateData)
    {
        // Formatting the string into date time info
        try
        {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, d MMM yyy HH:mm:ss", Locale.ENGLISH);
            LocalDateTime dateTime = LocalDateTime.parse(pubDateData, formatter);

            date = dateTime.toLocalDate();
            day = dateTime.getDayOfWeek();
            time = dateTime.toLocalTime();
        }
        catch (Exception e)
        {
            Log.e("MyTag", "Exception thrown during dat time handling: " + e.getMessage());
        }
    }

    /**
     *  Stores the earthquake's category
     * @param categoryData
     */
    public void SetCategory(String categoryData)
    {
        category = categoryData;
    }

    /**
     *  Stores the earthquake's latitude
     * @param geoLatData
     */
    public void SetLatitude(String geoLatData)
    {
        latitude = Float.parseFloat(geoLatData);
    }

    /**
     *  Stores the earthquake's longitude
     * @param geoLongData
     */
    public void SetLongitude(String geoLongData)
    {
        longitude = Float.parseFloat(geoLongData);
    }


    /*
    ========================================================================================================================================================================================================
    Getters
    ========================================================================================================================================================================================================
    */
    /**
     *  Gets the earthquake's magnitude from it's parsed description
     */
    public float GetStrengthValue()
    {
        float strength = Float.parseFloat(descriptionElements.get("Magnitude"));
        return strength;
    }


    /**
     *  Gets a color based on the earthquake's magnitude value
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public int GetStrengthColor()
    {
        float strength = GetStrengthValue();
        if (strength >= 0.0f && strength < 2.5f)
        {
            float lerp = (strength - 0.0f) / 2.5f;
            return Utility.ColorLerp(Color.BLUE, Color.GREEN, lerp);
        }
        else if (strength >= 2.5f && strength < 5.0f)
        {
            float lerp = (strength - 2.5f) / 2.5f;
            return Utility.ColorLerp(Color.GREEN, Color.YELLOW, lerp);
        }
        else if (strength >= 5.0f && strength < 7.5f)
        {
            float lerp = (strength - 5.0f) / 2.5f;
            return Utility.ColorLerp(Color.YELLOW, Color.parseColor("#FFA500"), lerp);
        }
        else
        {
            float lerp = (strength - 7.5f) / 2.5f;
            return Utility.ColorLerp(Color.parseColor("#FFA500"), Color.RED, lerp);
        }
    }


    /**
     *  Gets the earthquake's depth from it's parsed description
     */
    public float GetDepth()
    {
        String digits = descriptionElements.get("Depth").replaceAll("[^0-9.]", "");
        float depth = Float.parseFloat(digits);
        return depth;
    }
}
