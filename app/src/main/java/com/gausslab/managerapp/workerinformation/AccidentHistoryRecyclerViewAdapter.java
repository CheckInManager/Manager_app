package com.gausslab.managerapp.workerinformation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.gausslab.managerapp.databinding.ObjectAccidenthistoryBinding;
import com.gausslab.managerapp.model.AccidentHistory;
import com.gausslab.managerapp.todayworksite.OnItemInteractionListener;
import com.gausslab.managerapp.todayworksite.OnTodayWorksiteContextMenuInteractionListener;

import java.util.List;

public class AccidentHistoryRecyclerViewAdapter extends RecyclerView.Adapter<AccidentHistoryRecyclerViewAdapter.ViewHolder> {
    private List<AccidentHistory> accidentHistoryList;
    private WorkerInformationViewModel workerInformationViewModel;
    private OnItemInteractionListener listener;

    public AccidentHistoryRecyclerViewAdapter(List<AccidentHistory> accidentHistoryList, WorkerInformationViewModel workerInformationViewModel, OnItemInteractionListener<AccidentHistory> clickListener) {
        this.accidentHistoryList = accidentHistoryList;
        this.workerInformationViewModel = workerInformationViewModel;
        listener = clickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        return new ViewHolder(ObjectAccidenthistoryBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AccidentHistory currAccidentHistory = accidentHistoryList.get(position);
        holder.tv_description.setText(currAccidentHistory.getDescription());
        holder.tv_place.setText(currAccidentHistory.getPlace());
        holder.tv_date.setText(currAccidentHistory.getDate());
        holder.tv_time.setText(currAccidentHistory.getTime());
        holder.bt_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                workerInformationViewModel.deleteAccidentHistory(holder.tv_description.getText().toString(), holder.tv_place.getText().toString(),
                        holder.tv_date.getText().toString(),holder.tv_time.getText().toString());
            }
        });
        if(listener!=null && listener instanceof OnTodayWorksiteContextMenuInteractionListener){
            holder.cv_card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((OnTodayWorksiteContextMenuInteractionListener<AccidentHistory>) listener).onItemClick(accidentHistoryList.get(holder.getAdapterPosition()));
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return accidentHistoryList.size();
    }

    public void setAccidentHistoryList(List<AccidentHistory> newAccidentHistoryList){
        accidentHistoryList = newAccidentHistoryList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public final CardView cv_card;
        public final TextView tv_description;
        public final TextView tv_place;
        public final TextView tv_date;
        public final TextView tv_time;
        public final Button bt_delete;

        public ViewHolder(ObjectAccidenthistoryBinding binding) {
            super(binding.getRoot());
            cv_card = binding.objNoticeCard;
            tv_description = binding.objAccidenthistoryTvDescription;
            tv_place = binding.objAccidenthistoryTvPlace;
            tv_date = binding.objAccidenthistoryTvDate;
            tv_time = binding.objAccidenthistoryTvTime;
            bt_delete = binding.objAccidenthistoryBtDelete;
        }

        @Override
        public String toString() {
            return super.toString()+"";
        }
    }
}

