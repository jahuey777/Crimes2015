package com.example.jaimejahuey.criminalintent;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ShareCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
    private Button mDeleteButton;
    private Button mReportButton;
    private Button mSuspectButton;
    private Button mCallSuspectButon;

    private static final String ARG_CRIME_ID = "crime_id";
    private static final String DIALOG_DATE = "DialogDate";

    public static final int REQUEST_DATE = 0;
    private static final int REQUEST_TIME = 1;
    private static final int REQUEST_CONTACT = 2;
    private static final int REQUEST_CALL = 3;

    //This is what CrimeActivity calls.
    //The Bundles is done before the onCreate is called, we put here and get in onCreate
    public static CrimeFragment newInstance(UUID crimeId){
        Bundle args = new Bundle();
        args.putSerializable(ARG_CRIME_ID, crimeId);

        CrimeFragment fragment = new CrimeFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

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
        mDeleteButton = (Button) v.findViewById(R.id.crime_delete);
        mReportButton = (Button) v.findViewById(R.id.crime_report);
        mSuspectButton = (Button) v.findViewById(R.id.crime_suspect);
        mCallSuspectButon = (Button) v.findViewById(R.id.crime_call);

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

        mDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                SimpleDialog dialog = new SimpleDialog().newInstance(mCrime);

                dialog.show(fm, "dialog");
            }
        });

        mReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent i = new Intent(Intent.ACTION_SEND);
//                i.setType("text/plain");
//                i.putExtra(Intent.EXTRA_TEXT, getCrimeReport());
//                i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.crime_report_subject));
//
//                //give the user a list of apps to choose from.
//                i = Intent.createChooser(i, getString(R.string.send_report));

                //Doest the same as the above
                Intent intentBuilder =  ShareCompat.IntentBuilder.from(getActivity())
                        .setType("text/plain").setText(getCrimeReport()).setSubject(getString(R.string.crime_report_subject)).
                                setChooserTitle(getString(R.string.send_report)).createChooserIntent();
                startActivity(intentBuilder);
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


        //For the suspects.
        final Intent pickContact = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        mSuspectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(pickContact, REQUEST_CONTACT);
            }
        });

        if(mCrime.getmSuspect() != null){
            mSuspectButton.setText(mCrime.getmSuspect());
            mCallSuspectButon.setEnabled(true);

        }else {
            mCallSuspectButon.setEnabled(false);
        }

        //Checking to make sure the user has some app to choose a suspect from. (Some type of
        //COntact app
        PackageManager packageManager = getActivity().getPackageManager();
        //resolveactivy will try to match the intent, pickContact
        if(packageManager.resolveActivity(pickContact, PackageManager.MATCH_DEFAULT_ONLY)==null){
            mSuspectButton.setEnabled(false);
        }

        mCallSuspectButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSuspectNumber();
            }
        });

        return v;
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.fragment_crime, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        switch (item.getItemId()){
            case R.id.menu_item_delete_crime:
                FragmentManager fm = getActivity().getSupportFragmentManager();
                SimpleDialog dialog = new SimpleDialog().newInstance(mCrime);

                dialog.show(fm, "dialog");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
            updateTime();
        }
        else  if(requestCode == REQUEST_CONTACT && data!= null){
            //The contact name we clicked on
            Uri contactUri = data.getData();

            //Specify which fields you want your query to return values for.
            //Getting all of the contacts
            String[] queryFields = new String[]{
                    ContactsContract.Contacts.DISPLAY_NAME
            };
            //Perform your query - the contactURI is like a "where" clause here
            //getting the contact we need.
            Cursor c = getActivity().getContentResolver().query(contactUri, queryFields, null, null, null);

            try{
                //DOuble check that we got the results
                if(c.getCount()==0){
                    return;
                }
                //Pull out the first column of the first row of data, which is the name
                c.moveToFirst();
                String suspect = c.getString(0);
                mCrime.setmSuspect(suspect);
                mSuspectButton.setText(suspect);
                mCallSuspectButon.setEnabled(true);
            }finally {
                c.close();
            }
        }
    }

    private String getSuspectNumber(){

        //Pull out the first column of the first row of data, which is the ID
        String suspectName = mCrime.getmSuspect();

        String contactNumber = null;
        Cursor cursor = getActivity().getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null,
                    ContactsContract.Contacts.DISPLAY_NAME + " = ? ", new String[]{suspectName}, null);

        if (cursor.moveToFirst()) {
            String suspectID = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));

            //Getting all the phones for this contact
            Cursor phones = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "= ?", new String[]{suspectID}, null);
            //Grabbing all the numbers
            String[] listOfPhoneType = new String[phones.getCount()];
            String[] listOfPhoneNumbers = new String[phones.getCount()];
            int index = 0;
            while (phones.moveToNext()) {
                listOfPhoneNumbers[index] = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                int type = phones.getInt(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
                listOfPhoneType[index]= (String) ContactsContract.CommonDataKinds.Phone.getTypeLabel(getContext().getResources(), type, "");

                index++;
//                switch (type) {
//                    case ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE:
//                        contactNumber = number;
//                        Log.v("Hit case mobile ", contactNumber);
//                        Log.v("Content type2 " ,  " " + phoneType);
//                        break;
//                    case ContactsContract.CommonDataKinds.Phone.TYPE_HOME:
//                        contactNumber = number;
//                        Log.v("Hit case home ", contactNumber);
//                        Log.v("Content type " ,  " " + phoneType);
//                        break;
//                    case ContactsContract.CommonDataKinds.Phone.TYPE_WORK:
//                        break;
//                }
            }

            if(listOfPhoneNumbers.length>0 && listOfPhoneNumbers.length==1){
                return contactNumber;
            }
            else{
//                FragmentManager fragmentManager = getFragmentManager();
//                FragmentTransaction ft = fragmentManager.beginTransaction();
//
//                PickPhoneFragment fragment = PickPhoneFragment.newInstance();
//                ft.add(R.id.fragment_container, fragment);
//                ft.addToBackStack(null);
//
//                ft.commit();

                Intent i = new PickPhoneActivity().newIntent(getActivity(), listOfPhoneNumbers, listOfPhoneType);
                startActivity(i);

//                startActivity(new Intent(getActivity(), PickPhoneActivity.class));

            }
        }
        cursor.close();
        return contactNumber;
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

        if(mCrime.getmDate().getSeconds()<10) {
            sec = "0s";
        }

        if(mCrime.getmDate().getHours()>12) {
            hour = "0h:";
        }
        if(mCrime.getmDate().getMinutes()<10) {
            min = "0m:";
        }

        SimpleDateFormat formatter = new SimpleDateFormat(hour + min + sec);
        mTimeButton.setText(formatter.format(mCrime.getmDate().getTime()));
    }

    //For deleting a crime
    public static class SimpleDialog extends DialogFragment {

        Crime crimeDelete;

        public static SimpleDialog newInstance(Crime c) {
            Bundle args = new Bundle();
            args.putSerializable("Crime", c);

            SimpleDialog fragment = new SimpleDialog();
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            crimeDelete  = (Crime) getArguments().getSerializable("Crime");

            return new AlertDialog.Builder(getActivity())
                    .setMessage("Are you sure?").setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            CrimeLab crimes = CrimeLab.get(getActivity());
                            crimes.deleteCrime(crimeDelete.getmId());
                            getActivity().finish();

                        }
                    })
                    .setNegativeButton("No", null).create();
        }
    }

    @Override
    public void onPause(){
        super.onPause();

        //Udates the crime in the DB
        CrimeLab.get(getActivity()).updateCrime(mCrime);
    }

    public String getCrimeReport(){
        String solvedString = null;

        if(mCrime.ismSolved()){
            solvedString = getString(R.string.crime_report_solved);
        }else{
            solvedString = getString(R.string.crime_report_unsolved);
        }

        String dateFormat = "EEE, MMM dd";
        String dateString = android.text.format.DateFormat.format(dateFormat, mCrime.getmDate()).toString();

        String suspect = mCrime.getmSuspect();
        if(suspect==null) {
            suspect = getString(R.string.crime_report_no_suspect);
        } else{
            suspect = getString(R.string.crime_report_suspect, suspect);
        }

        String report = getString(R.string.crime_report, mCrime.getmTitle(), dateString, solvedString, suspect);

        return report;
    }
}
