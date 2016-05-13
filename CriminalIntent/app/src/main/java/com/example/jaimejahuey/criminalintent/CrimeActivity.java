package com.example.jaimejahuey.criminalintent;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;

import java.util.UUID;

//public class CrimeActivity extends FragmentActivity {
    public class CrimeActivity extends SingleFragmentActivity {

    private static final String EXTRA_CRIME_ID = "crime_id";

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_fragment);
//
//        //Fragment manager contains the back stack of fragment transactions
//        FragmentManager fm = getSupportFragmentManager();
//        Fragment fragment = fm.findFragmentById(R.id.fragment_container);
//
//        if(fragment==null){
//            fragment = new CrimeFragment();
//            fm.beginTransaction().add(R.id.fragment_container, fragment).commit();
//        }
//    }

    //Pass the context here and Id so that we can access them in the createFragment below
    //We can't get them in the createFragment directly, otherwise we would need to change createFragment,
    //which would cause us to change singleFragmentActivity as well
    public static Intent newIntent(Context packageContext, UUID crimeId){
        Intent intent = new Intent(packageContext, CrimeActivity.class);
        intent.putExtra(EXTRA_CRIME_ID, crimeId);

        return intent;
    }

    @Override
    protected Fragment createFragment(){
//        return new CrimeFragment();
        UUID crimeId = (UUID) getIntent().getSerializableExtra(EXTRA_CRIME_ID);
        return CrimeFragment.newInstance(crimeId);

    }
}
