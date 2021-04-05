package org.me.MobilePlatformDev_EarthquakeApp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{
    private Button listButton = null;
    private Button mapButton = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e("MyTag","onCreate: Main Activity");

        listButton = (Button)findViewById(R.id.startListButton);
        listButton.setOnClickListener(this);

        mapButton = (Button)findViewById(R.id.startMapButton);
        mapButton.setOnClickListener(this);
    }

    public void onClick(View view)
    {
        Log.e("MyTag","onClick: Main Activity");
        int id = view.getId();

        if (id == R.id.startListButton || id == R.id.startMapButton)
        {
            Intent newActivity = null;

            switch (id)
            {
                case (R.id.startListButton):
                {
                    // Getting the next page to move to (Earthquake List Page)
                    newActivity = new Intent(this, ListActivity.class);
                }
                break;

                case (R.id.startMapButton):
                {
                    // Getting the next page to move to (Earthquake List Page)
                    newActivity = new Intent(this, MapActivity.class);
                }
                break;
            }

            // Setting up activity switch animation
            Pair<View, String> p1 = Pair.create((View)this.findViewById(R.id.headerTitle), "TitleStartAnim");
            Pair<View, String> p2 = Pair.create((View)this.findViewById(R.id.headerSubtitle), "SubtitleStartAnim");
            Pair<View, String> p3 = Pair.create((View)this.findViewById(R.id.appIcon), "IconStartAnim");
            Pair<View, String> p4 = Pair.create((LinearLayout)this.findViewById(R.id.headerBackground), "BackgroundStartAnim");

            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this, p1, p2, p3, p4);

            // Switching activities
            startActivity(newActivity, options.toBundle());
        }
    }
}