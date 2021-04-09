/*
==========================================================================================================================================================================================================================================================
    Mobile Platform Development
    Ruari McGhee
    S1432402
==========================================================================================================================================================================================================================================================
*/

package org.me.MobilePlatformDev_EarthquakeApp;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DateCheckTask  extends AsyncTask<Void, Void, Void>
{
    private DateCheckerActivity mActivity = null;

    private LocalDate minDate = null;
    private LocalDate maxDate = null;

    private ArrayList<EarthquakeInfo> earthquakesInRange = null;

    public DateCheckTask(Context context, LocalDate minDate, LocalDate maxDate)
    {
        super();

        this.mActivity = (DateCheckerActivity)context;

        this.minDate = minDate;
        this.maxDate = maxDate;
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
        Log.e("MyTag", "A Sync Task Started: Checking Earthquakes In Date Range");

        earthquakesInRange = new ArrayList<>();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected Void doInBackground(Void... params)
    {
        ArrayList<EarthquakeInfo> infos = EarthquakeDataManager.Instance().GetEarthquakeInfos();
        for (int i = 0; i < infos.size(); i++)
        {
            LocalDate date = infos.get(i).date;
            boolean before = (minDate.isBefore(date) || minDate.isEqual(date));
            boolean after = (maxDate.isAfter(date) || maxDate.isEqual(date));
            if ( before && after)
            {
                earthquakesInRange.add(infos.get(i));
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result)
    {
        if (earthquakesInRange.size() == 0)
        {
            mActivity.ChangeState(DateCheckerActivity.DateCheckerActivityState.STATE_NOEARTHQUAKES);
        }
        else
        {
            // Checking earthquake extremes
            // Hashmap fully set up with the first earthquake in the range
            HashMap<Integer, EarthquakeInfo> extremes = new HashMap<>();
            extremes.put(R.id.earthquake_north, earthquakesInRange.get(0));
            extremes.put(R.id.earthquake_east, earthquakesInRange.get(0));
            extremes.put(R.id.earthquake_south, earthquakesInRange.get(0));
            extremes.put(R.id.earthquake_west, earthquakesInRange.get(0));
            extremes.put(R.id.earthquake_strongest, earthquakesInRange.get(0));
            extremes.put(R.id.earthquake_deepest, earthquakesInRange.get(0));
            extremes.put(R.id.earthquake_shallowest, earthquakesInRange.get(0));

            // Checking rest of the earthquakes in the range
            for (int i = 1; i < earthquakesInRange.size(); i++)
            {
                EarthquakeInfo info = earthquakesInRange.get(i);

                // Checking directions
                // Latitude
                if (info.latitude > extremes.get(R.id.earthquake_north).latitude)
                    extremes.put(R.id.earthquake_north, info);
                if (info.latitude < extremes.get(R.id.earthquake_south).latitude)
                    extremes.put(R.id.earthquake_south, info);
                // Longitude
                if (info.longitude > extremes.get(R.id.earthquake_east).longitude)
                    extremes.put(R.id.earthquake_east, info);
                if (info.longitude < extremes.get(R.id.earthquake_west).longitude)
                    extremes.put(R.id.earthquake_west, info);


                // Checking strength
                if (info.GetStrengthValue() > extremes.get(R.id.earthquake_strongest).GetStrengthValue())
                    extremes.put(R.id.earthquake_strongest, info);

                // Checking Depth
                if (info.GetDepth() > extremes.get(R.id.earthquake_deepest).GetDepth())
                    extremes.put(R.id.earthquake_deepest, info);
                if (info.GetDepth() < extremes.get(R.id.earthquake_shallowest).GetDepth())
                    extremes.put(R.id.earthquake_shallowest, info);
            }

            // Showing earthquake infos in the activity
            FragmentManager manager = mActivity.getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            for (Map.Entry<Integer, EarthquakeInfo> entry : extremes.entrySet())
            {
                Integer key = entry.getKey();
                EarthquakeInfo value = entry.getValue();

                Fragment newEarthquakeInfo = new EarthquakeInfoFragment(value);
                transaction.replace(key, newEarthquakeInfo);
            }
            transaction.commit();

            mActivity.ChangeState(DateCheckerActivity.DateCheckerActivityState.STATE_SHOWINFO);
        }
    }
}