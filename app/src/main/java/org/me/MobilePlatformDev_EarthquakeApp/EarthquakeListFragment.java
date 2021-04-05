package org.me.MobilePlatformDev_EarthquakeApp;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.fragment.app.ListFragment;

import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.time.format.DateTimeFormatter;
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
        public View getView(int position, View convertView, ViewGroup parent)
        {
            // Check if an existing view is being reused, otherwise inflate the view
            View view = convertView;
            if (view == null)
            {
                LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.earthquake_item_fragment, parent, false);
            }

            // Get the data item for this position
            EarthquakeInfo info = super.getItem(position);
            view.setTag(info);

            // Lookup view for data population
            TextView locationTextView = (TextView) view.findViewById(R.id.earthquakeLocation);
            TextView dateTextView = (TextView) view.findViewById(R.id.earthquakeDate);
            ImageView strengthImageView = (ImageView) view.findViewById(R.id.earthquakeStrengthIcon);
            TextView strengthTextView = (TextView) view.findViewById(R.id.earthquakeStrengthValue);

            view.setOnClickListener(new View.OnClickListener()
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

                    // Setting up activity switch animation
                    Pair<View, String> p1 = Pair.create((View)getActivity().findViewById(R.id.headerTitle), "TitleStartAnim");
                    Pair<View, String> p2 = Pair.create((View)getActivity().findViewById(R.id.headerSubtitle), "SubtitleStartAnim");
                    Pair<View, String> p3 = Pair.create((View)getActivity().findViewById(R.id.appIcon), "IconStartAnim");

                    ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), p1, p2, p3);

                    // Switching activities
                    startActivity(newActivity, options.toBundle());
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
            LayerDrawable icon = (LayerDrawable) strengthImageView.getDrawable();
            LayerDrawable changedIcon = (LayerDrawable) icon.mutate();
            changedIcon.findDrawableByLayerId(R.id.icon_background).setColorFilter(info.GetStrengthColor(), PorterDuff.Mode.MULTIPLY);
            strengthTextView.getBackground().setColorFilter(info.GetStrengthColor(), PorterDuff.Mode.MULTIPLY);
            strengthImageView.setImageDrawable(changedIcon);

            // Earthquake Strength (Text)
            strengthTextView.setText(String.valueOf(info.GetStrengthValue()));

            return view;
        }
    }
}
