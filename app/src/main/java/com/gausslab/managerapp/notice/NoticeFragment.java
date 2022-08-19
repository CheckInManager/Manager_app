package com.gausslab.managerapp.notice;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import com.gausslab.managerapp.R;
import com.gausslab.managerapp.databinding.FragmentNoticeBinding;
import com.gausslab.managerapp.model.Notice;
import com.gausslab.managerapp.todayworksite.OnTodayWorksiteContextMenuInteractionListener;

public class NoticeFragment extends Fragment {
    private FragmentNoticeBinding binding;
    private NoticeViewModel noticeViewModel;
    private RecyclerView rv_list;
    private Button bt_worksite;
    private Button bt_addWorker;
    private Button bt_notice;
    private Button bt_map;
    private Button bt_addNoticeForm;
    private NoticeRecyclerViewAdapter adapter;

    public NoticeFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        noticeViewModel = new ViewModelProvider(requireActivity()).get(NoticeViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNoticeBinding.inflate(inflater, container, false);
        rv_list = binding.noticeRvList;
        bt_worksite = binding.noticeBtWorksite;
        bt_addWorker = binding.noticeBtAddWorker;
        bt_notice = binding.noticeBtAddNotice;
        bt_map = binding.noticeBtMap;
        bt_addNoticeForm = binding.noticeBtAddNoticeForm;

        rv_list.setLayoutManager(new LinearLayoutManager(requireContext()));
        rv_list.addItemDecoration(new DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL));

        init();

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //region Observer
        noticeViewModel.isNoticeListLoaded().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLoaded) {
                if (isLoaded) {
                    adapter.setNoticeList(noticeViewModel.getNoticeList());
                }
            }
        });
        //endregion

        //region Listener
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
        //endregion
    }

    private void init() {
        noticeViewModel.loadNoticeList();
        setupAdapter();
    }

    private void setupAdapter() {
        adapter = new NoticeRecyclerViewAdapter(noticeViewModel.getNoticeList(), noticeViewModel,
                new OnTodayWorksiteContextMenuInteractionListener<Notice>() {
                    @Override
                    public void onItemClick(Notice obj) {
                        NoticeFragmentDirections.ActionNoticeFragmentToNoticeDetailFragment action = NoticeFragmentDirections.actionNoticeFragmentToNoticeDetailFragment();
                        action.setKeyValue(obj.getKeyValue());
                        NavHostFragment.findNavController(NoticeFragment.this).navigate(action);
                    }

                    @Override
                    public void onContextReturnWorksite(Notice obj) {

                    }
                });
        rv_list.setAdapter(adapter);
    }
}