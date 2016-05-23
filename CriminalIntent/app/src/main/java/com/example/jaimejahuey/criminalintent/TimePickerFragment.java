package com.example.jaimejahuey.criminalintent;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by jaimejahuey on 3/22/16.
 */
public class TimePickerFragment extends android.support.v4.app.DialogFragment
{
    public static final String EXTRA_TIME = "time";

    private Date mdate;

    public static TimePickerFragment newInstance(Date date)
    {
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_TIME, date);

        TimePickerFragment fragment = new TimePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        mdate = (Date) getArguments().getSerializable(EXTRA_TIME);

        //Create a Calendar to set the year, month and day using the mDate
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(mdate);

        int hour = calendar.get(Calendar.HOUR);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);

        View v = getActivity().getLayoutInflater().inflate(R.layout.dialog_time,null);

        TimePicker picker = (TimePicker)v.findViewById(R.id.dialog_timePicker);
        picker.setCurrentHour(hour);
        picker.setCurrentMinute(minute);

        picker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {

                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int monthOfYear = cal.get(Calendar.MONTH);
                int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);

                mdate = new GregorianCalendar(year, monthOfYear, dayOfMonth, hourOfDay, minute).getTime();

                // Update argument to preserve selected value on rotation
                getArguments().putSerializable(EXTRA_TIME, mdate);

            }
        });

        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(R.string.date_picker_title)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sendResult(Activity.RESULT_OK);
                    }
                })
                .create();
    }

    private void sendResult(int resultCode) {
        if (getTargetFragment() == null)
            return;

        Intent i = new Intent();
        i.putExtra(EXTRA_TIME, mdate);

        Log.v("Time ", " date being sent back from timepikcerfragment " + mdate.toString());

        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, i);
    }
}
