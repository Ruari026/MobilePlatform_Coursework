package org.me.MobilePlatformDev_EarthquakeApp;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Date;

public class DateCheckerActivity extends AppCompatActivity implements View.OnClickListener
{
    private ViewFlipper activityViewFlipper = null;

    private DateCheckerActivity.DateCheckerActivityState currentState = DateCheckerActivity.DateCheckerActivityState.STATE_LOADING;
    public enum DateCheckerActivityState
    {
        STATE_LOADING,
        STATE_NOEARTHQUAKES,
        STATE_SHOWINFO
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datechecker);
        Log.e("MyTag","onCreate: Date Checker Activity");

        activityViewFlipper = (ViewFlipper)findViewById(R.id.activityViewFlipper);

        ImageView loadingView = (ImageView)findViewById(R.id.loadingIcon);
        loadingView.setBackgroundResource(R.drawable.loading_drawable);
        AnimationDrawable loadingIcon = (AnimationDrawable)loadingView.getBackground();
        loadingIcon.start();

        Date fromDate = (Date) getIntent().getSerializableExtra("fromDateCheck");
        Date toDate = (Date) getIntent().getSerializableExtra("toDateCheck");
        if (fromDate == null || toDate == null)
        {
            Toast newToast = Toast.makeText(getBaseContext(), "Error: No Date Given...", Toast.LENGTH_LONG);
            newToast.show();
            this.finish();
        }
        else
        {
            new DateCheckTask(this).execute();
        }
    }

    public void onClick(View aview)
    {
        Log.e("MyTag","onClick: Date Checker Activity");
    }

    public DateCheckerActivity.DateCheckerActivityState GetCurrentState()
    {
        return currentState;
    }

    public void ChangeState(DateCheckerActivity.DateCheckerActivityState newState)
    {
        if (currentState == newState)
        {
            return;
        }

        currentState = newState;
        switch (currentState)
        {
            case STATE_LOADING:
            {
                activityViewFlipper.setDisplayedChild(0);
            }
            break;

            case STATE_NOEARTHQUAKES:
            {
                activityViewFlipper.setDisplayedChild(1);
            }
            break;

            case STATE_SHOWINFO:
            {
                activityViewFlipper.setDisplayedChild(2);
            }
            break;
        }
    }
}
