package com.gausslab.managerapp.checkinworkerbysite;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.widget.Button;
import android.widget.FrameLayout;

import com.gausslab.managerapp.R;
import com.gausslab.managerapp.databinding.FragmentCheckinworkersbysiteBinding;
import com.gausslab.managerapp.model.User;
import com.gausslab.managerapp.todayworksite.TodayWorkSiteListFragment;

import java.util.List;

public class CheckInWorkersBySiteFragment extends Fragment {

    private FragmentCheckinworkersbysiteBinding binding;
    private CheckInWorkersBySiteViewModel checkInWorkersBySiteViewModel;

    private FrameLayout fl_list;
    private Button bt_worksite;
    private Button bt_addWorker;
    private Button bt_addNotice;
    private Button bt_map;

    private List<User> userList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkInWorkersBySiteViewModel = new ViewModelProvider(requireActivity()).get(CheckInWorkersBySiteViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCheckinworkersbysiteBinding.inflate(inflater, container, false);
        fl_list = binding.checkinworkersbysiteFlList;
        bt_worksite = binding.checkinworkersbysiteBtWorksite;
        bt_addWorker = binding.checkinworkersbysiteBtAddWorker;
        bt_addNotice = binding.checkinworkersbysiteBtAddNotice;

        init();

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void init(){
        //userlist 로드해서 가져오기
        FragmentManager fm = getChildFragmentManager();
        Fragment myFrag = CheckInWorkersBySiteListFragment.newInstance(1, userList);
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(fl_list.getId(), myFrag);
        transaction.commit();
    }
}