package com.example.jaimejahuey.criminalintent;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by jaimejahuey on 4/15/16.
 */
public class CrimeListFragment extends Fragment{

    private RecyclerView mCrimeRecyclerView;
    private static int itemChange = 0;
    TextView textView;

    private CrimeAdapter mAdapter;

    //Visibility of toolbar
    private boolean mSubtitleVisible;

    //
    private static final String SAVED_SUBUTITLE_VISIBLE = "subtitle";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        //third parameter is false since we are adding the view in the code
        //second param is the parent
        View view = inflater.inflate(R.layout.fragment_crime_list, container, false);
        textView = (TextView) view.findViewById(R.id.crime_list_textview);

        emptyView(textView);

        //LinearLayoutManger is required, or a type of setLayoutManger
        //It will arrange the items in a vertical list
        mCrimeRecyclerView = (RecyclerView) view.findViewById(R.id.crime_recycler_view);
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //Saving the value of the mSubtitleVisible
        if(savedInstanceState !=null){
            mSubtitleVisible = savedInstanceState.getBoolean(SAVED_SUBUTITLE_VISIBLE);
        }

        updateUI();

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVED_SUBUTITLE_VISIBLE, mSubtitleVisible);
    }

    public void emptyView(TextView textView){

        if(CrimeLab.get(getActivity()).getCrimes().size()==0){
            textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT));
            textView.setText("Add Crime");
            textView.setTextSize(Float.parseFloat("20"));
        }
        else {
            textView.setText("");
            textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
        }
    }

    private void updateUI(){
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        List<Crime> crimes = crimeLab.getCrimes();

        if(mAdapter==null){
            Log.v("Size recreated ", " " + crimes.size());

            mAdapter = new CrimeAdapter(crimes);
            //Giving the recyclerView the crimes to display
            mCrimeRecyclerView.setAdapter(mAdapter);
        }
        else{
            mAdapter.setmCrimes(crimes);
            mAdapter.notifyDataSetChanged();
        }
//            mAdapter.notifyItemChanged(itemChange);

        //Will update the subtitle in Onresume and onCreate
        updateSubtitle();
    }

    //Displays the subtitle
    private void updateSubtitle(){
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        int crimeCount = crimeLab.getCrimes().size();
        String subtitle = getString(R.string.subtitle_format, crimeCount);

        //Set to null whenever we tap hide subitle
        if(!mSubtitleVisible){
            subtitle= null;
        }

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setSubtitle(subtitle);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.fragment_crime_list, menu);

        //Hiding or showing the subtitle. Using boolean to keep track,
        //This helps on rotation of the screen when view recreated
        MenuItem subtitleItem = menu.findItem(R.id.menu_item_show_subtitle);

        Log.v("visiblt ","" + mSubtitleVisible);

        if(mSubtitleVisible){
            subtitleItem.setTitle(R.string.hide_subtitle);
        }
        else{
            subtitleItem.setTitle(R.string.show_subtitle);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        switch (item.getItemId()){
            case R.id.menu_item_new_crime:
                Crime crime = new Crime();
                CrimeLab.get(getActivity()).addCrime(crime);
                Intent intent = CrimePagerActivity.newIntent(getActivity(), crime.getmId());
                startActivity(intent);
                return true;
            case R.id.menu_item_show_subtitle:
                mSubtitleVisible = !mSubtitleVisible;
                Log.v("visible ","" + mSubtitleVisible);
                //Causes menu to be recreated
                getActivity().invalidateOptionsMenu();
                updateSubtitle();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        emptyView(textView);
        updateUI();
    }
    private class CrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private Crime mCrime;

        private TextView mTitleTextView;
        private TextView mDateTextView;
        private CheckBox mSolvedCheckBox;

        public CrimeHolder(View itemView){
            super(itemView);
            itemView.setOnClickListener(this);

            mTitleTextView = (TextView) itemView.findViewById(R.id.list_item_title);
            mDateTextView = (TextView) itemView.findViewById(R.id.list_item_date);
            mSolvedCheckBox = (CheckBox) itemView.findViewById(R.id.list_item_crime_checkbox);

        }

        public void bindCrime(Crime crime){
            mCrime = crime;
            mTitleTextView.setText(mCrime.getmTitle());
            mDateTextView.setText(mCrime.getmDate().toString());
            mSolvedCheckBox.setChecked(mCrime.ismSolved());
            mSolvedCheckBox.setEnabled(false);
        }

        @Override
        public void onClick(View view){
//            Toast.makeText(getActivity(), mCrime.getmTitle() + " clicked!", Toast.LENGTH_SHORT).show();

//            Intent i = new Intent(getActivity(), CrimeActivity.class);
//            Intent intent = new CrimeActivity().newIntent(getActivity(), mCrime.getmId());
            Intent intent = new CrimePagerActivity().newIntent(getActivity(), mCrime.getmId());

            CrimeLab crimeLab = CrimeLab.get(getActivity());
            itemChange = crimeLab.getPosition(mCrime.getmId());

            startActivity(intent);
        }
    }

    private class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder>{

        private List<Crime> mCrimes;

        public CrimeAdapter(List<Crime> crimes){
            mCrimes = crimes;
        }

        @Override
        public CrimeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflator = LayoutInflater.from(getActivity());

            View view = layoutInflator.inflate(R.layout.list_item_crime,parent,false);

            return new CrimeHolder(view);
        }

        @Override
        public void onBindViewHolder(CrimeHolder holder, int position) {
            Crime crime = mCrimes.get(position);

//            holder.mTitleTextView.setText(crime.getmTitle());
            holder.bindCrime(crime);
        }

        @Override
        public int getItemCount() {
            return mCrimes.size();
        }

        //Sets the crimes whenever the user presses back.
        //If they press up then the activity is reacreated along with the adapter
        public void setmCrimes(List<Crime> crimes){
            mCrimes = crimes;
        }
    }
}
