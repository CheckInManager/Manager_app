package com.gausslab.managerapp.todayworksite;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
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
import android.widget.Toast;

import com.gausslab.managerapp.R;
import com.gausslab.managerapp.model.Worksite;
import com.gausslab.managerapp.databinding.FragmentTodayworksiteBinding;

import java.util.Calendar;
import java.util.List;

public class TodayWorkSiteFragment extends Fragment {
    private FragmentTodayworksiteBinding binding;
    private TodayWorkSiteViewModel todayWorkSiteViewModel;

    private FrameLayout fl_list;
    private Button bt_worksite;
    private Button bt_addWorker;
    private Button bt_notice;
    private Button bt_map;
    private Button bt_addWorksite;

    private List<Worksite> todayWorksiteList;

    private long pressedTime = 0;
    private String todayCal;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        todayWorkSiteViewModel = new ViewModelProvider(requireActivity()).get(TodayWorkSiteViewModel.class);
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (System.currentTimeMillis() > pressedTime + 2000) {
                    pressedTime = System.currentTimeMillis();
                    Toast.makeText(requireContext(), R.string.toast_beforeExit, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(requireContext(), R.string.toast_exit, Toast.LENGTH_SHORT).show();
                    requireActivity().finish();
                }
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentTodayworksiteBinding.inflate(inflater, container, false);

        fl_list = binding.todayworksiteFlList;
        bt_worksite = binding.todayworksiteBtWorksite;
        bt_addWorker = binding.todayworksiteBtAddWorker;
        bt_notice = binding.todayworksiteBtAddNotice;
        bt_map = binding.todayworksiteMap;
        bt_addWorksite = binding.todayworksiteBtAddWorksite;

        init();

        View root = binding.getRoot();
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //region Listener
        bt_addWorksite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(TodayWorkSiteFragment.this).navigate(R.id.action_todayWorkSiteFragment_to_worksiteFormFragment);
            }
        });

        bt_addWorker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(TodayWorkSiteFragment.this).navigate(R.id.action_todayWorkSiteFragment_to_addWorkerFragment);
            }
        });

        bt_notice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(TodayWorkSiteFragment.this).navigate(R.id.action_todayWorkSiteFragment_to_noticeFragment);
            }
        });
        //endregion
    }

    private void init() {
        convertDateFormat();
        todayWorkSiteViewModel.loadTodayWorksite(todayCal);
        todayWorksiteList = todayWorkSiteViewModel.getTodayWorksite();

        FragmentManager fm = getChildFragmentManager();
        Fragment myFrag = TodayWorkSiteListFragment.newInstance(1, todayWorksiteList);
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(fl_list.getId(), myFrag);
        transaction.commit();
    }

    private void convertDateFormat() {
        Calendar cal = Calendar.getInstance();
        todayCal = ((cal.get(Calendar.YEAR)) + "" + (cal.get(Calendar.MONTH) + 1) + "" + (cal.get(Calendar.DATE)));

        String todayMonthCal = ((cal.get(Calendar.MONTH) + 1) + "");
        String todayDayCal = ((cal.get(Calendar.DATE)) + "");

        if (todayMonthCal.length() < 2 && todayDayCal.length() < 2) {
            todayCal = ((cal.get(Calendar.YEAR)) + "0" + (cal.get(Calendar.MONTH) + 1) + "0" + (cal.get(Calendar.DATE)));
        } else if (todayMonthCal.length() < 2) {
            todayCal = ((cal.get(Calendar.YEAR)) + "0" + (cal.get(Calendar.MONTH) + 1) + "" + (cal.get(Calendar.DATE)));
        } else if (todayDayCal.length() < 2) {
            todayCal = ((cal.get(Calendar.YEAR)) + "" + (cal.get(Calendar.MONTH) + 1) + "0" + (cal.get(Calendar.DATE)));
        }
    }
}