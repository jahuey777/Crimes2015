package com.example.jaimejahuey.criminalintent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * Created by jaimejahuey on 4/14/16.
 */
public class CrimeFragment extends Fragment {

    private Crime mCrime;
    private EditText mTitleField;
    private Button mDateButton;
    private Button mTimeButton;
    private CheckBox mSolvedCheckbox;

    private static final String ARG_CRIME_ID = "crime_id";
    private static final String DIALOG_DATE = "DialogDate";

    public static final int REQUEST_DATE = 0;
    private static final int REQUEST_TIME = 1;


    //This is what CrimeActivity calls.
    //The Bundles is done before the onCreate is called, we put here and get in onCreate
    public static CrimeFragment newInstance(UUID crimeId){
        Bundle args = new Bundle();
        args.putSerializable(ARG_CRIME_ID, crimeId);

        CrimeFragment fragment = new CrimeFragment();
        fragment.setArguments(args);

        Log.v("Happens in newInstance", "1");
        return fragment;
    }

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.v("after newIstance", "2");

//        mCrime = new Crime();
//        UUID crimeId = (UUID) getActivity().getIntent().getSerializableExtra(CrimeActivity.EXTRA_CRIME_ID);
        UUID crimeId = (UUID) getArguments().getSerializable(ARG_CRIME_ID);
        mCrime = CrimeLab.get(getActivity()).getCrime(crimeId);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_crime,container,false);

        mTitleField = (EditText) v.findViewById(R.id.crime_title_hint);
        mTitleField.setText(mCrime.getmTitle());
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mCrime.setmTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mDateButton = (Button) v.findViewById(R.id.crime_date);
        mTimeButton = (Button) v.findViewById(R.id.crime_time);

//        mDateButton.setEnabled(false);
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();

//                DatePickerFragment dialog = new DatePickerFragment();
                DatePickerFragment dialog = DatePickerFragment.newInstance(mCrime.getmDate());
                dialog.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
                dialog.show(manager, DIALOG_DATE);
            }
        });

        mTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager fm = getActivity().getSupportFragmentManager();
                TimePickerFragment dialog = TimePickerFragment.newInstance(mCrime.getmDate());

                dialog.setTargetFragment(CrimeFragment.this, REQUEST_TIME);
                dialog.show(fm, "hi");
            }
        });

        //Formatting the date
        UpdateDate();
        updateTime();

        mSolvedCheckbox = (CheckBox) v.findViewById(R.id.crime_solved);
        mSolvedCheckbox.setChecked(mCrime.ismSolved());
        mSolvedCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mCrime.setmSolved(isChecked);
            }
        });

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){

        if(resultCode!= Activity.RESULT_OK){
            return;
        }
        if (requestCode == REQUEST_DATE) {
            Date date = (Date)data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);

            mCrime.setmDate(date);
            UpdateDate();
        }
        else if (requestCode==REQUEST_TIME) {

            Date date = (Date)data.getSerializableExtra(TimePickerFragment.EXTRA_TIME);

            mCrime.setmDate(date);

            Log.v("here", "time");
            updateTime();
        }
    }

    private void UpdateDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("EEEE, MMM d, yyyy.");
        mDateButton.setText(formatter.format(mCrime.getmDate()).toString());
    }

    public void updateTime()
    {
        String min = "m:";
        String hour = "h:";
        String sec = "s";

        if(mCrime.getmDate().getSeconds()<10)
        {
            sec = "0s";
        }
        Log.v("Hours", " " + mCrime.getmDate().getHours());
        Log.v("Hoursmins", " " + mCrime.getmDate().getMinutes());
        Log.v("Hourssec", " " + mCrime.getmDate().getMinutes());

        if(mCrime.getmDate().getHours()>12)
        {
            hour = "0h:";
        }
        if(mCrime.getmDate().getMinutes()<10)
        {
            min = "0m:";
        }

        SimpleDateFormat formatter = new SimpleDateFormat(hour + min + sec);
        mTimeButton.setText(formatter.format(mCrime.getmDate().getTime()));

    }
}
