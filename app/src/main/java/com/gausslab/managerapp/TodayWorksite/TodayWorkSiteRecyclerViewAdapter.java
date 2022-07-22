package com.gausslab.managerapp.TodayWorksite;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gausslab.managerapp.model.Worksite;
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
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder. workName.setText(worksiteList.get(position).getWorkName());
    }

    @Override
    public int getItemCount(){ return worksiteList.size();}

    public void setWorksiteList(List<Worksite> newWorksiteList){
        worksiteList = newWorksiteList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public final TextView workName;

        public ViewHolder(ObjectTodayworksiteBinding binding){
            super(binding.getRoot());
            workName = binding.todayworksiteTvWorkName;
        }
        @Override
        public String toString(){return super.toString()+"";}
    }
}
