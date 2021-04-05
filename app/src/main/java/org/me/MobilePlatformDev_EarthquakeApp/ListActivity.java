package org.me.MobilePlatformDev_EarthquakeApp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.ListFragment;

import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class ListActivity extends AppCompatActivity implements View.OnClickListener
{
    public TextView titleText = null;
    public TextView subtitleText = null;
    public ImageView appIcon = null;
    private Button optionsButton = null;

    private Fragment dateOptionsFragment = null;

    private ListActivityState currentState = ListActivityState.STATE_SCROLLABLELIST;
    public enum ListActivityState
    {
        STATE_SCROLLABLELIST,
        STATE_DATEOPTIONS
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Log.e("MyTag","onCreate: List Activity");

        titleText = (TextView)findViewById(R.id.headerTitle);
        subtitleText = (TextView)findViewById(R.id.headerSubtitle);
        appIcon = (ImageView)findViewById(R.id.appIcon);
        optionsButton = (Button)findViewById(R.id.button);
        optionsButton.setOnClickListener(this);

        new XmlListParsingTask(this).execute();
    }

    public void onClick(View aview)
    {
        Log.e("MyTag","onClick: List Activity");
        ChangeState(ListActivityState.STATE_DATEOPTIONS);
    }

    public ListActivityState GetCurrentState()
    {
        return currentState;
    }

    public void ChangeState(ListActivityState newState)
    {
        if (currentState == newState)
        {
            return;
        }

        currentState = newState;
        switch (currentState)
        {
            case STATE_SCROLLABLELIST:
            {
                if (dateOptionsFragment != null)
                {
                    FragmentManager manager = getSupportFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();
                    transaction.remove(dateOptionsFragment);
                    transaction.commit();

                    dateOptionsFragment = null;
                }
            }
            break;

            case STATE_DATEOPTIONS:
            {
                dateOptionsFragment = new EarthquakeDateOptionsFragment();

                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.dateOptionsFragment, dateOptionsFragment);
                transaction.commit();
            }
            break;
        }
    }
}
