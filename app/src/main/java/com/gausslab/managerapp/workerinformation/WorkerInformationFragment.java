package com.gausslab.managerapp.workerinformation;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gausslab.managerapp.R;
import com.gausslab.managerapp.databinding.FragmentCheckinworkersbysiteBinding;
import com.gausslab.managerapp.model.User;

import java.util.List;

public class WorkerInformationFragment extends Fragment {
    private FragmentCheckinworkersbysiteBinding binding;

    private List<User> userList;


    public WorkerInformationFragment() {
    }

    public static WorkerInformationFragment newInstance(String param1, String param2) {
        WorkerInformationFragment fragment = new WorkerInformationFragment();
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
        return inflater.inflate(R.layout.fragment_workerinformation, container, false);
    }
}