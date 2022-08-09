package com.gausslab.managerapp.workerinformation;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
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

        public ViewHolder(ObjectAccidenthistoryBinding binding) {
            super(binding.getRoot());
        }

        @Override
        public String toString() {
            return super.toString()+"";
        }
    }
}

