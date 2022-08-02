package com.gausslab.managerapp.todayworksite;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
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
        holder.workName.setText(currWorksite.getWorksiteName());
        if (listener != null && listener instanceof OnTodayWorksiteContextMenuInteractionListener) {
            holder.card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((OnTodayWorksiteContextMenuInteractionListener<Worksite>) listener).onItemClick(worksiteList.get(holder.getAdapterPosition()));
                }
            });
            holder.card.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
                @Override
                public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                    menu.setHeaderTitle("Select Action");
                    MenuItem returnWorksite = menu.add(Menu.NONE, 1, 1, "Return Worksite");
                    returnWorksite.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            ((OnTodayWorksiteContextMenuInteractionListener<Worksite>) listener).onContextReturnWorksite(worksiteList.get(holder.getAdapterPosition()));
                            return true;
                        }
                    });
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
        public final TextView workName;

        public ViewHolder(ObjectTodayworksiteBinding binding) {
            super(binding.getRoot());
            card = binding.objTodayworksiteCard;
            workName = binding.objTodayworksiteTvWorkName;
        }

        @Override
        public String toString() {
            return super.toString() + "";
        }
    }
}
