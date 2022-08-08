package com.gausslab.managerapp.checkinworkerbysite;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gausslab.managerapp.R;
import com.gausslab.managerapp.databinding.FragmentCheckedinworkersbysiteBinding;
import com.gausslab.managerapp.model.User;
import com.gausslab.managerapp.todayworksite.OnTodayWorksiteContextMenuInteractionListener;

public class CheckedInWorkersBySiteFragment extends Fragment {

    private FragmentCheckedinworkersbysiteBinding binding;
    private CheckedInWorkersBySiteViewModel checkedInWorkersBySiteViewModel;

    private Button bt_worksite;
    private Button bt_addWorker;
    private Button bt_addNotice;
    private Button bt_map;
    private ImageView iv_qr;
    private TextView tv_worksiteName;
    private RecyclerView rv_userList;
    private CheckedInWorkersBySiteRecyclerViewAdapter adapter;

    private String worksiteName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkedInWorkersBySiteViewModel = new ViewModelProvider(requireActivity()).get(CheckedInWorkersBySiteViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCheckedinworkersbysiteBinding.inflate(inflater, container, false);
        rv_userList = binding.checkedInWorkersBySiteRvUserList;
        bt_worksite = binding.checkedInWorkersBySiteBtWorksite;
        bt_addWorker = binding.checkedInWorkersBySiteBtAddWorker;
        bt_addNotice = binding.checkedInWorkersBySiteBtAddNotice;
        bt_map = binding.checkedInWorkersBySiteBtMap;
        iv_qr = binding.checkedinworkersbysiteIvQr;
        tv_worksiteName = binding.checkedinworkersbysiteTvWorksiteName;

        rv_userList.setLayoutManager(new LinearLayoutManager(requireContext()));
        rv_userList.addItemDecoration(new DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL));

        init();

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tv_worksiteName.setText(worksiteName);

        //region Observer
        checkedInWorkersBySiteViewModel.isUserListLoaded().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLoaded) {
                if (isLoaded) {
                    adapter.setUserList(checkedInWorkersBySiteViewModel.getUserList());
                }
            }
        });

        checkedInWorkersBySiteViewModel.isQrImageLoaded().observe(getViewLifecycleOwner(), isQrLoaded -> {
            if (isQrLoaded) {
                iv_qr.setImageDrawable(checkedInWorkersBySiteViewModel.getQrImage());
            }
        });
        //endregion

        //region Listener
        bt_worksite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(CheckedInWorkersBySiteFragment.this).navigate(R.id.action_checkInWorkdersBySiteFragment_to_todayWorkSiteFragment);
            }
        });

        bt_addWorker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(CheckedInWorkersBySiteFragment.this).navigate(R.id.action_checkInWorkdersBySiteFragment_to_addWorkerFragment);
            }
        });
        //endregion
    }

    private void init() {
        worksiteName = CheckedInWorkersBySiteFragmentArgs.fromBundle(getArguments()).getWorksiteName();
        checkedInWorkersBySiteViewModel.setWorksite(CheckedInWorkersBySiteFragmentArgs.fromBundle(getArguments()).getWorksiteName(),
                CheckedInWorkersBySiteFragmentArgs.fromBundle(getArguments()).getWorksiteName() + CheckedInWorkersBySiteFragmentArgs.fromBundle(getArguments()).getWorksiteLocation() + CheckedInWorkersBySiteFragmentArgs.fromBundle(getArguments()).getWorksiteStartDate());
        checkedInWorkersBySiteViewModel.loadUserListByWorksite(worksiteName);
        setupAdapter();
    }

    private void setupAdapter() {
        adapter = new CheckedInWorkersBySiteRecyclerViewAdapter(checkedInWorkersBySiteViewModel.getUserList(),
                new OnTodayWorksiteContextMenuInteractionListener<User>() {
                    @Override
                    public void onItemClick(User obj) {
                        String phoneNumber = obj.getPhoneNumber();
                        CheckedInWorkersBySiteFragmentDirections.ActionCheckInWorkdersBySiteFragmentToUserInformationFragment action = CheckedInWorkersBySiteFragmentDirections.actionCheckInWorkdersBySiteFragmentToUserInformationFragment();
                        action.setPhoneNumber(phoneNumber);
                        NavHostFragment.findNavController(CheckedInWorkersBySiteFragment.this).navigate(action);
                    }

                    @Override
                    public void onContextReturnWorksite(User obj) {

                    }
                });
        rv_userList.setAdapter(adapter);
    }
}