package com.gausslab.managerapp.TodayWorksite;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gausslab.managerapp.Worksite;
import com.gausslab.managerapp.databinding.ObjectTodayworksiteBinding;

import java.util.List;

public class TodayWorkSiteRecyclerViewAdapter extends RecyclerView.Adapter<TodayWorkSiteRecyclerViewAdapter.ViewHolder> {
    private List<Worksite> worksiteList;
    private TodayWorkSiteViewModel todayWorkSiteViewModel;

    public TodayWorkSiteRecyclerViewAdapter(List<Worksite> items, TodayWorkSiteViewModel tvm) {
        worksiteList = items;
        todayWorkSiteViewModel = tvm;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ObjectTodayworksiteBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull TodayWorkSiteRecyclerViewAdapter.ViewHolder holder, int position) {

    }
}
