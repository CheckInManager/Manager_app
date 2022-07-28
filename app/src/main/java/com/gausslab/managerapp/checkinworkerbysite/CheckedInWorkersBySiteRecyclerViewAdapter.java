package com.gausslab.managerapp.checkinworkerbysite;

import android.graphics.Color;
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

import com.gausslab.managerapp.databinding.ObjectCheckedinworkersbysiteBinding;
import com.gausslab.managerapp.model.User;
import com.gausslab.managerapp.todayworksite.OnItemInteractionListener;
import com.gausslab.managerapp.todayworksite.OnTodayWorksiteContextMenuInteractionListener;

import java.util.List;

public class CheckedInWorkersBySiteRecyclerViewAdapter extends RecyclerView.Adapter<CheckedInWorkersBySiteRecyclerViewAdapter.ViewHolder> {
    private List<User> userList;
    private OnItemInteractionListener<User> listener;

    public CheckedInWorkersBySiteRecyclerViewAdapter(List<User> items) {
        userList = items;
    }

    public CheckedInWorkersBySiteRecyclerViewAdapter(List<User> items, OnItemInteractionListener<User> clickListener) {
        userList = items;
        listener = clickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ObjectCheckedinworkersbysiteBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        User currUser = userList.get(position);
        holder.tv_userName.setText(currUser.getUserName());
        holder.tv_phoneNum.setText(currUser.getPhoneNumber());

        if (currUser.getAccidentHistory().length() != 0) {
            holder.card.setBackgroundColor(Color.RED);
        } else {
            holder.card.setBackgroundColor(Color.WHITE);
        }

        if (listener != null && listener instanceof OnTodayWorksiteContextMenuInteractionListener) {
            holder.card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((OnTodayWorksiteContextMenuInteractionListener<User>) listener).onItemClick(userList.get(holder.getAdapterPosition()));
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public void setUserList(List<User> newUserList) {
        userList = newUserList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final CardView card;
        public final TextView tv_userName;
        public final TextView tv_phoneNum;

        public ViewHolder(ObjectCheckedinworkersbysiteBinding binding) {
            super(binding.getRoot());
            card = binding.objCheckinworkersbysiteCard;
            tv_userName = binding.objCheckinworkersbysiteTvUserName;
            tv_phoneNum = binding.objCheckinworkersbysiteTvPhonenum;
        }

        @Override
        public String toString() {
            return super.toString() + "";
        }
    }
}
