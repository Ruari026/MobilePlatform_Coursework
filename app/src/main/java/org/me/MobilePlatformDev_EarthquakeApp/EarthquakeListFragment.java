package org.me.MobilePlatformDev_EarthquakeApp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class EarthquakeListFragment extends ListFragment
{
    private List<EarthquakeInfo> info;

    public EarthquakeListFragment()
    {
        super();
    }
    public EarthquakeListFragment(List<EarthquakeInfo> theInfo)
    {
        super();
        info = theInfo;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.earthquake_list_fragment, container, false);
        EarthquakesAdapter adapter = new EarthquakesAdapter(this.getContext());
        setListAdapter(adapter);

        return view;
    }

    private class EarthquakesAdapter extends ArrayAdapter<EarthquakeInfo>
    {
        public EarthquakesAdapter(Context context)
        {
            super(context, 0, EarthquakeDataManager.Instance().GetEarthquakeInfos());
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            // Get the data item for this position
            EarthquakeInfo info = getItem(position);

            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null)
            {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.earthquake_item_fragment, parent, false);
                convertView.setTag(info);
            }

            // Lookup view for data population
            TextView locationTextView = (TextView) convertView.findViewById(R.id.earthquakeLocation);
            TextView dateTextView = (TextView) convertView.findViewById(R.id.earthquakeDate);
            ImageView strengthImageView = (ImageView) convertView.findViewById(R.id.earthquakeStrengthIcon);
            TextView strengthTextView = (TextView) convertView.findViewById(R.id.earthquakeStrengthValue);

            convertView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    EarthquakeInfo info = (EarthquakeInfo) view.getTag();
                    Log.i("MyTag", info.title);

                    // Getting the next page to move to (Earthquake List Page)
                    Log.e("MyTag","onClick: Main Activity");
                    Intent newActivity = new Intent(view.getContext(), DetailsActivity.class);

                    // Passing the selected earthquake info to the next activity
                    newActivity.putExtra("EarthquakeInfo", info);

                    // Switching activities
                    startActivity(newActivity);
                }
            });

            // Populate the data into the template view using the data object
            // Earthquake Location
            String location = info.descriptionElements.get("Location");
            location = location.replace(",", ", ");
            locationTextView.setText(location);

            // Earthquake Date & Time
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MM yy");
            String dateTime = info.date.format(formatter).replace(" ", "-");
            dateTextView.setText(dateTime);

            // Earthquake Strength (Color)
            float strength = Float.parseFloat(info.descriptionElements.get("Magnitude"));
            int color = Color.BLACK;
            if (strength >= 0.0f && strength < 2.5f)
            {
                float lerp = (strength - 0.0f) / 2.5f;
                color = Utility.ColorLerp(Color.BLUE, Color.GREEN, lerp);
            }
            else if (strength >= 2.5f && strength < 5.0f)
            {
                float lerp = (strength - 2.5f) / 2.5f;
                color = Utility.ColorLerp(Color.GREEN, Color.YELLOW, lerp);
            }
            else if (strength >= 5.0f && strength < 7.5f)
            {
                float lerp = (strength - 5.0f) / 2.5f;
                color = Utility.ColorLerp(Color.YELLOW, Color.parseColor("#FFA500"), lerp);
            }
            else if (strength >= 7.5f && strength < 10.0f)
            {
                float lerp = (strength - 7.5f) / 2.5f;
                color = Utility.ColorLerp(Color.parseColor("#FFA500"), Color.RED, lerp);
            }
            LayerDrawable icon = (LayerDrawable) strengthImageView.getDrawable();
            icon.findDrawableByLayerId(R.id.icon_background).setColorFilter(color, PorterDuff.Mode.MULTIPLY);
            strengthTextView.getBackground().setColorFilter(color, PorterDuff.Mode.MULTIPLY);

            // Earthquake Strength (Text)
            strengthTextView.setText(info.descriptionElements.get("Magnitude"));

            return convertView;
        }
    }
}
