package com.gausslab.managerapp.TodayWorksite;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import com.gausslab.managerapp.databinding.FragmentTodayworksiteBinding;

public class TodayWorkSiteFragment extends Fragment {

    private FragmentTodayworksiteBinding binding;
    private TodayWorkSiteViewModel todayWorkSiteViewModel;

    private FrameLayout fl_list;
    private Button bt_worksite;
    private Button bt_addWork;
    private Button bt_addNotice;
    private Button bt_map;
    private Button bt_addWorksite;


    public static TodayWorkSiteFragment newInstance(String param1, String param2) {
        TodayWorkSiteFragment fragment = new TodayWorkSiteFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        todayWorkSiteViewModel = new ViewModelProvider(requireActivity()).get(TodayWorkSiteViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentTodayworksiteBinding.inflate(inflater, container, false);
        fl_list = binding.todayworksiteFlList;
        bt_worksite = binding.todayworksiteBtWorksite;
        bt_addWork = binding.todayworksiteBtAddWorker;
        bt_addNotice = binding.todayworksiteBtAddNotice;
        bt_map = binding.todayworksiteMap;
        bt_addWorksite=binding.todayworksiteBtAddWorksite;

        init();

        View root = binding.getRoot();
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void init(){
        //todayWorkSiteViewModel.loadTodayWorksite();
        //FM

    }
}