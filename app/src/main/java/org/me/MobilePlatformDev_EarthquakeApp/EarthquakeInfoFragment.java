/*
==========================================================================================================================================================================================================================================================
    Mobile Platform Development
    Ruari McGhee
    S1432402
==========================================================================================================================================================================================================================================================
*/

package org.me.MobilePlatformDev_EarthquakeApp;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;

import java.time.format.DateTimeFormatter;

public class EarthquakeInfoFragment extends Fragment
{
    EarthquakeInfo theInfo = null;

    public EarthquakeInfoFragment()
    {
        super(R.layout.earthquake_item_fragment);
    }
    public EarthquakeInfoFragment(EarthquakeInfo info)
    {
        super(R.layout.earthquake_item_fragment);
        theInfo = info;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(View view, Bundle bundle)
    {
        if (theInfo == null)
            return;

        view.setTag(theInfo);

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
        String location = theInfo.descriptionElements.get("Location");
        location = location.replace(",", ", ");
        locationTextView.setText(location);

        // Earthquake Date & Time
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MM yy");
        String dateTime = theInfo.date.toString();
        dateTextView.setText(dateTime);

        // Earthquake Strength (Color)
        LayerDrawable icon = (LayerDrawable) strengthImageView.getDrawable();
        LayerDrawable changedIcon = (LayerDrawable) icon.mutate();
        changedIcon.findDrawableByLayerId(R.id.icon_background).setColorFilter(theInfo.GetStrengthColor(), PorterDuff.Mode.MULTIPLY);
        strengthTextView.getBackground().setColorFilter(theInfo.GetStrengthColor(), PorterDuff.Mode.MULTIPLY);
        strengthImageView.setImageDrawable(changedIcon);

        // Earthquake Strength (Text)
        strengthTextView.setText(String.valueOf(theInfo.GetStrengthValue()));
    }
}
