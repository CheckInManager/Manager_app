package com.gausslab.managerapp.checkinworkerbysite;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gausslab.managerapp.App;
import com.gausslab.managerapp.R;
import com.gausslab.managerapp.databinding.FragmentCheckedinworkersbysiteBinding;
import com.gausslab.managerapp.model.AccidentHistory;
import com.gausslab.managerapp.model.User;
import com.gausslab.managerapp.repository.WorksiteRepository;
import com.gausslab.managerapp.todayworksite.OnTodayWorksiteContextMenuInteractionListener;

import java.util.ArrayList;
import java.util.List;

public class CheckedInWorkersBySiteFragment extends Fragment {

    private FragmentCheckedinworkersbysiteBinding binding;
    private CheckedInWorkersBySiteViewModel checkedInWorkersBySiteViewModel;
    private Button bt_worksite;
    private Button bt_addWorker;
    private Button bt_notice;
    private Button bt_map;
    private ImageView iv_qr;
    private TextView tv_worksiteName;
    private RecyclerView rv_userList;
    private CheckedInWorkersBySiteRecyclerViewAdapter adapter;
    private String key;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkedInWorkersBySiteViewModel = new ViewModelProvider(requireActivity()).get(CheckedInWorkersBySiteViewModel.class);
        key = CheckedInWorkersBySiteFragmentArgs.fromBundle(getArguments()).getKeyValue();

        checkedInWorkersBySiteViewModel.loadWorksite(key);
        checkedInWorkersBySiteViewModel.setWorksite(key);

        adapter = new CheckedInWorkersBySiteRecyclerViewAdapter(
                new ArrayList<>(),
                new OnCheckedInWorkersInteractionListener() {
                    @Override
                    public void onClick(User obj) {
                        String phoneNumber = obj.getPhoneNumber();
                        CheckedInWorkersBySiteFragmentDirections.ActionCheckInWorkdersBySiteFragmentToUserInformationFragment action = CheckedInWorkersBySiteFragmentDirections.actionCheckInWorkdersBySiteFragmentToUserInformationFragment();
                        action.setPhoneNumber(phoneNumber);
                        action.setUserName(obj.getUserName());
                        NavHostFragment.findNavController(CheckedInWorkersBySiteFragment.this).navigate(action);
                    }

                    @Override
                    public void onDelete(User obj) {
                        checkedInWorkersBySiteViewModel.deleteUser(obj);
                    }
                });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCheckedinworkersbysiteBinding.inflate(inflater, container, false);
        rv_userList = binding.checkedInWorkersBySiteRvUserList;
        bt_worksite = binding.checkedInWorkersBySiteBtWorksite;
        bt_addWorker = binding.checkedInWorkersBySiteBtAddWorker;
        bt_notice = binding.checkedInWorkersBySiteBtAddNotice;
        bt_map = binding.checkedInWorkersBySiteBtMap;
        iv_qr = binding.checkedinworkersbysiteIvQr;
        tv_worksiteName = binding.checkedinworkersbysiteTvWorksiteName;

        rv_userList.setLayoutManager(new LinearLayoutManager(requireContext()));
        rv_userList.addItemDecoration(new DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL));

        rv_userList.setAdapter(adapter);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //region Observer
        checkedInWorkersBySiteViewModel.getUserList().observe(getViewLifecycleOwner(), new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                adapter.setUserList(users);
            }
        });

        checkedInWorkersBySiteViewModel.isQrImageLoaded().observe(getViewLifecycleOwner(), isQrLoaded -> {
            if (isQrLoaded) {
                iv_qr.setImageDrawable(checkedInWorkersBySiteViewModel.getQrImage());
            }
        });

        checkedInWorkersBySiteViewModel.isWorksiteLoaded().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLoaded) {
                if (isLoaded) {
                    tv_worksiteName.setText(checkedInWorkersBySiteViewModel.getWorksiteName());
                }
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

        bt_notice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(CheckedInWorkersBySiteFragment.this).navigate(R.id.action_checkInWorkdersBySiteFragment_to_noticeFragment);
            }
        });

        iv_qr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mail_intent = new Intent(Intent.ACTION_SEND);
                mail_intent.setType("*/*");
                Uri uri = FileProvider.getUriForFile(requireActivity(), App.getFileProvider(), checkedInWorkersBySiteViewModel.getQrFile(key));
                mail_intent.putExtra(Intent.EXTRA_STREAM, uri);
                mail_intent.putExtra(Intent.EXTRA_SUBJECT, "emailTest"); //이메일 제목
                mail_intent.putExtra(Intent.EXTRA_TEXT, "test"); //메일 내용
                startActivity(mail_intent);
            }
        });
        //endregion
    }
}