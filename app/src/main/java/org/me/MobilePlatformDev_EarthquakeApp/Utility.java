package org.me.MobilePlatformDev_EarthquakeApp;

import android.graphics.Color;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalDateTime;

public class Utility
{
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static int ColorLerp(int min, int max, float t)
    {
        if (t < 0)
            t = 0;

        if (t > 1)
            t = 1;

        Color minColor = Color.valueOf(min);
        Color maxColor = Color.valueOf(max);

        float r = (minColor.red() * (1.0f - t)) + (maxColor.red() * t);
        float g = (minColor.green() * (1.0f - t)) + (maxColor.green() * t);
        float b = (minColor.blue() * (1.0f - t)) + (maxColor.blue() * t);
        float a = (minColor.alpha() * (1.0f - t)) + (maxColor.alpha() * t);

        int newColor = Color.argb(a, r, g, b);
        return newColor;
    }

    public static Boolean CheckTimes(LocalDateTime min, LocalDateTime max, float difference)
    {


        return false;
    }
}
