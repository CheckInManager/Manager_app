package com.gausslab.managerapp.todayworksite;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import com.gausslab.managerapp.R;
import com.gausslab.managerapp.model.Worksite;
import com.gausslab.managerapp.databinding.FragmentTodayworksiteBinding;

import java.util.List;

public class TodayWorkSiteFragment extends Fragment {

    private FragmentTodayworksiteBinding binding;
    private TodayWorkSiteViewModel todayWorkSiteViewModel;

    private FrameLayout fl_list;
    private Button bt_worksite;
    private Button bt_addWork;
    private Button bt_addNotice;
    private Button bt_map;
    private Button bt_addWorksite;

    private List<Worksite> todayWorksiteList;


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
        bt_addWorksite = binding.todayworksiteBtAddWorksite;

        init();

        View root = binding.getRoot();
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bt_addWorksite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(TodayWorkSiteFragment.this).navigate(R.id.action_todayWorkSiteFragment_to_worksiteFormFragment);
            }
        });

    }

    private void init() {
        todayWorkSiteViewModel.loadTodayWorksite();
        todayWorksiteList = todayWorkSiteViewModel.getTodayWorksite();
        FragmentManager fm = getChildFragmentManager();
        Fragment myFrag = TodayWorkSiteListFragment.newInstance(1, todayWorksiteList);
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(fl_list.getId(), myFrag);
        transaction.commit();
    }
}