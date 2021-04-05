package org.me.MobilePlatformDev_EarthquakeApp;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;

public class DateCheckTask  extends AsyncTask<Void, Void, Void>
{
    private DateCheckerActivity mActivity = null;
    private ArrayList<EarthquakeInfo> earthquakesInRange = null;

    public DateCheckTask(Context context)
    {
        super();
        mActivity = (DateCheckerActivity)context;
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

    @Override
    protected Void doInBackground(Void... params)
    {
        return null;
    }

    @Override
    protected void onPostExecute(Void result)
    {
        if (earthquakesInRange.size() == 0)
        {
            mActivity.ChangeState(DateCheckerActivity.DateCheckerActivityState.STATE_LOADING);
        }
        else
        {
            mActivity.ChangeState(DateCheckerActivity.DateCheckerActivityState.STATE_SHOWINFO);
        }
    }
}