package org.me.MobilePlatformDev_EarthquakeApp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
            TextView linkTextView = (TextView) convertView.findViewById(R.id.earthquakeLink);
            View buttonView = convertView.findViewById(R.id.earthquake_item);

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
            String location = info.descriptionElements.get("Location");
            location = location.replace(",", ", ");
            locationTextView.setText(location);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MM yy");
            String dateTime = info.date.format(formatter).replace(" ", "-");
            dateTextView.setText(dateTime);

            linkTextView.setText(info.link);

            // Return the completed view to render on screen
            return convertView;
        }
    }
}
