package com.gausslab.managerapp.notice;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import com.gausslab.managerapp.R;
import com.gausslab.managerapp.databinding.FragmentNoticeBinding;

public class NoticeFragment extends Fragment {
    private FragmentNoticeBinding binding;

    private FrameLayout fl_list;
    private Button bt_worksite;
    private Button bt_addWorker;
    private Button bt_notice;
    private Button bt_map;
    private Button bt_addNoticeForm;

    public NoticeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNoticeBinding.inflate(inflater, container, false);

        fl_list = binding.noticeFlList;
        bt_worksite = binding.noticeBtWorksite;
        bt_addWorker = binding.noticeBtAddWorker;
        bt_notice = binding.noticeBtAddNotice;
        bt_map = binding.noticeBtMap;
        bt_addNoticeForm = binding.noticeBtAddNoticeForm;

        View root = binding.getRoot();
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bt_worksite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(NoticeFragment.this).navigate(R.id.action_noticeFragment_to_todayWorkSiteFragment);
            }
        });

        bt_addWorker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(NoticeFragment.this).navigate(R.id.action_noticeFragment_to_addWorkerFragment);
            }
        });

        bt_addNoticeForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(NoticeFragment.this).navigate(R.id.action_noticeFragment_to_addNewNoticeFormFragment);
            }
        });
    }
}