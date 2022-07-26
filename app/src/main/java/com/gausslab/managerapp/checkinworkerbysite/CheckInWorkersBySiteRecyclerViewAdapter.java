package com.gausslab.managerapp.checkinworkerbysite;

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

import com.gausslab.managerapp.databinding.ObjectCheckinworkersbysiteBinding;
import com.gausslab.managerapp.model.User;
import com.gausslab.managerapp.model.Worksite;
import com.gausslab.managerapp.todayworksite.OnItemInteractionListener;
import com.gausslab.managerapp.todayworksite.OnTodayWorksiteContextMenuInteractionListener;

import java.util.List;

public class CheckInWorkersBySiteRecyclerViewAdapter extends RecyclerView.Adapter<CheckInWorkersBySiteRecyclerViewAdapter.ViewHolder> {
    private List<User> userList;
    private CheckInWorkersBySiteViewModel checkInWorkersBySiteViewModel;

    public CheckInWorkersBySiteRecyclerViewAdapter(List<User> items, CheckInWorkersBySiteViewModel cvm) {
        userList = items;
        checkInWorkersBySiteViewModel = cvm;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        return new ViewHolder(ObjectCheckinworkersbysiteBinding.inflate(LayoutInflater.from(parent.getContext()), parent,false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position){
        User currUser = userList.get(position);
        holder.tv_userName.setText(currUser.getUserName());
        holder.tv_phoneNum.setText(currUser.getPhoneNumber());

    }

    @Override
    public int getItemCount(){return userList.size();}

    public void setUserList(List<User> newUserList){
        userList = newUserList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public final CardView card;
        public final TextView tv_userName;
        public final TextView tv_phoneNum;

        public ViewHolder(ObjectCheckinworkersbysiteBinding binding){
            super(binding.getRoot());
            card = binding.objCheckinworkersbysiteCard;
            tv_userName = binding.objCheckinworkersbysiteTvUserName;
            tv_phoneNum = binding.objCheckinworkersbysiteTvPhonenum;
        }

        @Override
        public String toString(){return  super.toString()+"";}
    }
}
