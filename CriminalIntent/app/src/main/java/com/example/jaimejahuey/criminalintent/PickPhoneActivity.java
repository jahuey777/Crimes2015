package com.example.jaimejahuey.criminalintent;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.UUID;

public class PickPhoneActivity extends SingleFragmentActivity {

    private static String EXTRA_PHONE_NUMBERS = "numbers";
    private static String EXTRA_PHONE_LABELS = "labels";

    //Order this works.
    //Called from CrimeFragment->To this method -> SingleFrag onCreate -> this createFragment -> newInstance PPfragment
    //onCreate PPfragment -> oncreateView PPfragment
    public static Intent newIntent(Context packageContext,String [] phoneNumbers, String[] phoneLabels ){
        Intent intent = new Intent(packageContext, PickPhoneActivity.class);
        intent.putExtra(EXTRA_PHONE_LABELS, phoneLabels);
        intent.putExtra(EXTRA_PHONE_NUMBERS,phoneNumbers);

        return intent;
    }

    @Override
    protected Fragment createFragment() {
        String [] labels = (String[])getIntent().getSerializableExtra(EXTRA_PHONE_LABELS);
        String [] numbers = (String[]) getIntent().getSerializableExtra(EXTRA_PHONE_NUMBERS);

        return PickPhoneFragment.newInstance(numbers, labels);
    }
}
