package com.example.jaimejahuey.criminalintent;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.UUID;

/**
 * Created by jaimejahuey on 5/25/16.
 */
public class PickPhoneFragment extends Fragment{

    private static final String ARG_LABELS = "labels";
    private static final String ARG_NUMBERS = "numbers";

    private String [] labels;
    private String [] numbers;

    //This is what PickPhoneActivity calls.
    //The Bundles is done before the onCreate is called, we put here and get in onCreate
    public static PickPhoneFragment newInstance(String [] phoneNumbers, String [] phoneLabels){
        Bundle args = new Bundle();
        args.putSerializable(ARG_NUMBERS, phoneNumbers );
        args.putSerializable(ARG_LABELS, phoneLabels);

        PickPhoneFragment fragment = new PickPhoneFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        numbers = (String[]) getArguments().getSerializable(ARG_NUMBERS);
        labels = (String[]) getArguments().getSerializable(ARG_LABELS);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_pick_phone, container, false);

        /* Example of how to lanuch a fragment form this one. NOte that it uses the singleFragment Conatiner, or activity_fragment xml*/
//        Button button = (Button) view.findViewById(R.id.test);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                FragmentManager fragmentManager = getFragmentManager();
//                FragmentTransaction ft = fragmentManager.beginTransaction();
//
//                CrimeListFragment fragment = new CrimeListFragment();
//                ft.replace(R.id.fragment_container, fragment);
//                ft.addToBackStack(null);
//
//                ft.commit();
//            }
//        });



        return view;
    }
}
