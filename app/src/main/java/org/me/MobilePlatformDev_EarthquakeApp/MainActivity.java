package org.me.MobilePlatformDev_EarthquakeApp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{
    private TextView titleText;
    private TextView subtitleText;
    private Button startButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e("MyTag","onCreate: Main Activity");

        titleText = (TextView)findViewById(R.id.salutation);
        subtitleText = (TextView)findViewById(R.id.acknowledgement);

        startButton = (Button)findViewById(R.id.startButton);
        startButton.setOnClickListener(this);
    }

    public void onClick(View aview)
    {
        // Getting the next page to move to (Earthquake List Page)
        Log.e("MyTag","onClick: Main Activity");
        Intent newActivity = new Intent(this, ListActivity.class);

        // Setting up activity switch animation
        // For the Main -> List page switch this only affects the Title Text, SubTitle Text, & Main Button
        Pair<View, String> p1 = Pair.create((View)titleText, "TitleStartAnim");
        Pair<View, String> p2 = Pair.create((View)subtitleText, "SubtitleStartAnim");
        //Pair<View, String> p3 = Pair.create((View)startButton, "ButtonStartAnim");
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this, p1, p2);

        // Switching activities
        startActivity(newActivity, options.toBundle());
    }
}