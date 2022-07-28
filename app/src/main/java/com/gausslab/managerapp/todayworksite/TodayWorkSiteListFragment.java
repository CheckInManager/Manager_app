package com.gausslab.managerapp.todayworksite;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gausslab.managerapp.R;
import com.gausslab.managerapp.model.Worksite;

import java.util.List;

public class TodayWorkSiteListFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;

    private List<Worksite> worksiteList;
    private TodayWorkSiteViewModel todayWorkSiteViewModel;
    private TodayWorkSiteRecyclerViewAdapter adapter;

    public TodayWorkSiteListFragment() {

    }

    public TodayWorkSiteListFragment(List<Worksite> worksiteList) {
        this.worksiteList = worksiteList;
    }

    public static TodayWorkSiteListFragment newInstance(int columnCount, List<Worksite> worksiteList) {
        TodayWorkSiteListFragment fragment = new TodayWorkSiteListFragment(worksiteList);
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_todayworksite_list, container, false);

        todayWorkSiteViewModel = new ViewModelProvider(requireActivity()).get(TodayWorkSiteViewModel.class);

        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            adapter = new TodayWorkSiteRecyclerViewAdapter(worksiteList, new OnTodayWorksiteContextMenuInteractionListener<Worksite>() {
                @Override
                public void onItemClick(Worksite obj) {
                    String worksiteName = obj.getWorkName();
                    TodayWorkSiteFragmentDirections.ActionTodayWorkSiteFragmentToCheckInWorkdersBySiteFragment action = TodayWorkSiteFragmentDirections.actionTodayWorkSiteFragmentToCheckInWorkdersBySiteFragment();
                    action.setWorksiteName(worksiteName);
                    NavHostFragment.findNavController(TodayWorkSiteListFragment.this).navigate(action);
                }

                @Override
                public void onContextReturnWorksite(Worksite obj) {

                }
            });
            recyclerView.addItemDecoration(new DividerItemDecoration(view.getContext(), LinearLayoutManager.VERTICAL));
            recyclerView.setAdapter(adapter);
        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        todayWorkSiteViewModel.todayWorksiteListLoaded().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLoaded) {
                if (isLoaded) {
                    adapter.setWorksiteList(todayWorkSiteViewModel.getTodayWorksite());
                }
            }
        });
    }
}
