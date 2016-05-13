package com.example.jaimejahuey.criminalintent;

import android.support.v4.app.Fragment;

/**
 * Created by jaimejahuey on 4/15/16.
 */
public class CrimeListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment();
    }
}
