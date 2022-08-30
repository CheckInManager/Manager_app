package com.gausslab.managerapp.checkinworkerbysite;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.gausslab.managerapp.databinding.ObjectCheckedinworkersbysiteBinding;
import com.gausslab.managerapp.model.User;

import java.util.List;

public class CheckedInWorkersBySiteRecyclerViewAdapter extends RecyclerView.Adapter<CheckedInWorkersBySiteRecyclerViewAdapter.ViewHolder> {
    private List<User> userList;
    private OnCheckedInWorkersInteractionListener listener;

    public CheckedInWorkersBySiteRecyclerViewAdapter(List<User> userList, OnCheckedInWorkersInteractionListener clickListener) {
        this.userList = userList;
        listener = clickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ObjectCheckedinworkersbysiteBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        User currUser = userList.get(position);
        holder.tv_userName.setText(currUser.getName());
        holder.tv_phoneNum.setText(currUser.getPhoneNumber());
        if (currUser.isAccidentHistory()) {
            holder.cv_card.setBackgroundColor(Color.RED);
        }
        holder.bt_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onDelete(currUser);
            }
        });

        holder.cv_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClick(userList.get(holder.getAdapterPosition()));
            }
        });
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
        public final CardView cv_card;
        public final TextView tv_userName;
        public final TextView tv_phoneNum;
        public final Button bt_delete;

        public ViewHolder(ObjectCheckedinworkersbysiteBinding binding) {
            super(binding.getRoot());
            cv_card = binding.objCheckinworkersbysiteCard;
            tv_userName = binding.objCheckinworkersbysiteTvUserName;
            tv_phoneNum = binding.objCheckinworkersbysiteTvPhonenum;
            bt_delete = binding.objCheckinworkersbysiteBtDelete;
        }

        @Override
        public String toString() {
            return super.toString() + "";
        }
    }
}
