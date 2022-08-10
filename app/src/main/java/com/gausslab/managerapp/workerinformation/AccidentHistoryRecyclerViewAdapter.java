package com.gausslab.managerapp.workerinformation;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.gausslab.managerapp.databinding.ObjectAccidenthistoryBinding;
import com.gausslab.managerapp.model.AccidentHistory;

import java.util.List;

public class AccidentHistoryRecyclerViewAdapter extends RecyclerView.Adapter<AccidentHistoryRecyclerViewAdapter.ViewHolder> {
    private List<AccidentHistory> accidentHistoryList;
    private WorkerInformationViewModel workerInformationViewModel;

    public AccidentHistoryRecyclerViewAdapter(List<AccidentHistory> accidentHistoryList, WorkerInformationViewModel workerInformationViewModel) {
        this.accidentHistoryList = accidentHistoryList;
        this.workerInformationViewModel = workerInformationViewModel;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        return new ViewHolder(ObjectAccidenthistoryBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AccidentHistory currAccidentHistory = accidentHistoryList.get(position);
        holder.tv_decsription.setText(currAccidentHistory.getDescription());
        holder.tv_place.setText(currAccidentHistory.getPlace());
        holder.tv_date.setText(currAccidentHistory.getDate());
        holder.tv_time.setText(currAccidentHistory.getTime());
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
        public final TextView tv_decsription;
        public final TextView tv_place;
        public final TextView tv_date;
        public final TextView tv_time;

        public ViewHolder(ObjectAccidenthistoryBinding binding) {
            super(binding.getRoot());
            cv_card = binding.objNoticeCard;
            tv_decsription = binding.objAccidenthistoryTvDescription;
            tv_place = binding.objAccidenthistoryTvPlace;
            tv_date = binding.objAccidenthistoryTvDate;
            tv_time = binding.objAccidenthistoryTvTime;
        }

        @Override
        public String toString() {
            return super.toString()+"";
        }
    }
}

