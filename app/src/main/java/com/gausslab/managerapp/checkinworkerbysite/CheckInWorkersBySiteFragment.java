package com.gausslab.managerapp.checkinworkerbysite;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gausslab.managerapp.R;

public class CheckInWorkersBySiteFragment extends Fragment {



    public CheckInWorkersBySiteFragment() {
        // Required empty public constructor
    }


    public static CheckInWorkersBySiteFragment newInstance(String param1, String param2) {
        CheckInWorkersBySiteFragment fragment = new CheckInWorkersBySiteFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_checkinworkersbysite, container, false);
    }
}