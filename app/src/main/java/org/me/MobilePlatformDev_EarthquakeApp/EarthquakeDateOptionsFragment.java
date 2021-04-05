package org.me.MobilePlatformDev_EarthquakeApp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

public class EarthquakeDateOptionsFragment extends Fragment
{
    ImageButton closeButton = null;

    EditText singleDatePicker = null;
    EditText fromDatePicker = null;
    EditText toDatePicker = null;

    Button singleDateButton = null;
    Button rangeDateButton = null;


    public EarthquakeDateOptionsFragment()
    {
        super(R.layout.earthquake_dateoptions_fragment);
    }

    @Override
    public void onViewCreated(View view, Bundle bundle)
    {
        closeButton = view.findViewById(R.id.closeButton);

        singleDatePicker = (EditText)view.findViewById(R.id.singleDateField);
        fromDatePicker = (EditText)view.findViewById(R.id.fromDateField);
        toDatePicker = (EditText)view.findViewById(R.id.toDateField);

        singleDateButton = (Button)view.findViewById(R.id.singleDateButton);
        rangeDateButton = (Button)view.findViewById(R.id.rangeDateButton);

        closeButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ListActivity activity = (ListActivity)getActivity();
                activity.ChangeState(ListActivity.ListActivityState.STATE_SCROLLABLELIST);
            }
        });

        singleDatePicker.setOnClickListener(new View.OnClickListener()
        {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v)
            {
                DatePickerDialog dialog = new DatePickerDialog(getContext());
                dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                dialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener()
                {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth)
                    {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(year, month, dayOfMonth);

                        Date date = calendar.getTime();
                        String dateString =  new SimpleDateFormat("dd-MMMM-yy").format(date);

                        singleDatePicker.setText(dateString);
                        singleDatePicker.setTag(date);
                        dialog.hide();
                    }
                });
                dialog.show();
            }
        });
        fromDatePicker.setOnClickListener(new View.OnClickListener()
        {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v)
            {
                DatePickerDialog dialog = new DatePickerDialog(getContext());
                dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                dialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener()
                {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth)
                    {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(year, month, dayOfMonth);

                        Date date = calendar.getTime();
                        String dateString =  new SimpleDateFormat("dd-MMMM-yy").format(date);

                        fromDatePicker.setText(dateString);
                        fromDatePicker.setTag(date);
                        dialog.hide();
                    }
                });
                dialog.show();
            }
        });
        toDatePicker.setOnClickListener(new View.OnClickListener()
        {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v)
            {
                DatePickerDialog dialog = new DatePickerDialog(getContext());
                dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                dialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener()
                {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth)
                    {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(year, month, dayOfMonth);

                        Date date = calendar.getTime();
                        String dateString =  new SimpleDateFormat("dd-MMMM-yy").format(date);

                        toDatePicker.setText(dateString);
                        toDatePicker.setTag(date);
                        dialog.hide();
                    }
                });
                dialog.show();
            }
        });

        singleDateButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Date dateToCheck = (Date)singleDatePicker.getTag();

                if (dateToCheck == null)
                {
                    Toast newToast = Toast.makeText(getContext(), "Please Input A Date To Check", Toast.LENGTH_SHORT);
                    newToast.show();
                }
                else
                {
                    // Getting the next page to move to (Earthquake Date Checker Page)
                    Intent newActivity = new Intent(view.getContext(), DateCheckerActivity.class);

                    // Passing the selected earthquake info to the next activity
                    newActivity.putExtra("fromDateCheck", dateToCheck);
                    newActivity.putExtra("toDateCheck", dateToCheck);

                    // Setting up activity switch animation
                    Pair<View, String> p1 = Pair.create((View)getActivity().findViewById(R.id.headerTitle), "TitleStartAnim");
                    Pair<View, String> p2 = Pair.create((View)getActivity().findViewById(R.id.headerSubtitle), "SubtitleStartAnim");
                    Pair<View, String> p3 = Pair.create((View)getActivity().findViewById(R.id.appIcon), "IconStartAnim");

                    ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), p1, p2, p3);

                    // Switching activities
                    startActivity(newActivity, options.toBundle());
                }
            }
        });
        rangeDateButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Date fromDate = (Date)fromDatePicker.getTag();
                Date toDate = (Date)toDatePicker.getTag();

                if (fromDate == null)
                {
                    Toast newToast = Toast.makeText(getContext(), "Please Input A Date To Check From", Toast.LENGTH_SHORT);
                    newToast.show();
                }
                else if (toDate == null)
                {
                    Toast newToast = Toast.makeText(getContext(), "Please Input A Date To Check To", Toast.LENGTH_SHORT);
                    newToast.show();
                }
                else
                {
                    // Getting the next page to move to (Earthquake Date Checker Page)
                    Intent newActivity = new Intent(view.getContext(), DateCheckerActivity.class);

                    // Passing the selected earthquake info to the next activity
                    newActivity.putExtra("fromDateCheck", fromDate);
                    newActivity.putExtra("toDateCheck", toDate);

                    // Setting up activity switch animation
                    Pair<View, String> p1 = Pair.create((View)getActivity().findViewById(R.id.headerTitle), "TitleStartAnim");
                    Pair<View, String> p2 = Pair.create((View)getActivity().findViewById(R.id.headerSubtitle), "SubtitleStartAnim");
                    Pair<View, String> p3 = Pair.create((View)getActivity().findViewById(R.id.appIcon), "IconStartAnim");

                    ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), p1, p2, p3);

                    // Switching activities
                    startActivity(newActivity, options.toBundle());
                }
            }
        });
    }
}
