package com.gausslab.managerapp.todayworksite;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.gausslab.managerapp.model.Worksite;
import com.gausslab.managerapp.databinding.ObjectTodayworksiteBinding;

import java.util.List;

public class TodayWorkSiteRecyclerViewAdapter extends RecyclerView.Adapter<TodayWorkSiteRecyclerViewAdapter.ViewHolder> {
    private List<Worksite> worksiteList;
    private TodayWorkSiteViewModel todayWorkSiteViewModel;
    private OnItemInteractionListener<Worksite> listener;

    public TodayWorkSiteRecyclerViewAdapter(List<Worksite> items, TodayWorkSiteViewModel tvm) {
        worksiteList = items;
        todayWorkSiteViewModel = tvm;
    }

    public TodayWorkSiteRecyclerViewAdapter(List<Worksite> items, OnItemInteractionListener<Worksite> clickListener) {
        worksiteList = items;
        listener = clickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ObjectTodayworksiteBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Worksite currWorksite = worksiteList.get(position);
        holder.worksiteName.setText(currWorksite.getWorksiteName());
        if (listener != null && listener instanceof OnTodayWorksiteContextMenuInteractionListener) {
            holder.card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((OnTodayWorksiteContextMenuInteractionListener<Worksite>) listener).onItemClick(worksiteList.get(holder.getAdapterPosition()));
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return worksiteList.size();
    }

    public void setWorksiteList(List<Worksite> newWorksiteList) {
        worksiteList = newWorksiteList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final CardView card;
        public final TextView worksiteName;

        public ViewHolder(ObjectTodayworksiteBinding binding) {
            super(binding.getRoot());
            card = binding.objTodayworksiteCard;
            worksiteName = binding.objTodayworksiteTvWorkName;
        }

        @Override
        public String toString() {
            return super.toString() + "";
        }
    }
}
