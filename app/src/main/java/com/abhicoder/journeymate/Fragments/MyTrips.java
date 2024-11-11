package com.abhicoder.journeymate.Fragments;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.abhicoder.journeymate.Activities.Traveldate;
import com.abhicoder.journeymate.Activities.Travelling;
import com.abhicoder.journeymate.R;


public class MyTrips extends Fragment {



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_my_trips, container, false);

        Button startNewTripButton = view.findViewById(R.id.startNewTripButton);


        startNewTripButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getContext(), Travelling.class);
                startActivity(intent);
            }
        });

        return view;
    }
}