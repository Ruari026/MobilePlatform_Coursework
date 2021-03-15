package org.me.MobilePlatformDev_EarthquakeApp;

import java.util.ArrayList;

public class EarthquakeDataManager
{
    ArrayList<EarthquakeInfo> theInfo = null;

    private static EarthquakeDataManager instance = null;
    public static EarthquakeDataManager Instance()
    {
        if (instance == null)
        {
            instance = new EarthquakeDataManager();
        }

        return instance;
    }
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
}