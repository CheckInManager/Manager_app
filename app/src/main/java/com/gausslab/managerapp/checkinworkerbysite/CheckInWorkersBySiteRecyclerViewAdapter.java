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

import com.gausslab.managerapp.databinding.ObjectCheckinworkersbysiteBinding;
import com.gausslab.managerapp.model.User;
import com.gausslab.managerapp.model.Worksite;
import com.gausslab.managerapp.todayworksite.OnItemInteractionListener;
import com.gausslab.managerapp.todayworksite.OnTodayWorksiteContextMenuInteractionListener;

import java.util.List;

public class CheckInWorkersBySiteRecyclerViewAdapter extends RecyclerView.Adapter<CheckInWorkersBySiteRecyclerViewAdapter.ViewHolder> {
    private List<User> userList;
    private CheckInWorkersBySiteViewModel checkInWorkersBySiteViewModel;
    private OnItemInteractionListener<User> listener;

    public CheckInWorkersBySiteRecyclerViewAdapter(List<User> items, CheckInWorkersBySiteViewModel cvm) {
        userList = items;
        checkInWorkersBySiteViewModel = cvm;
    }

    public CheckInWorkersBySiteRecyclerViewAdapter(List<User> items, CheckInWorkersBySiteViewModel cvm, OnItemInteractionListener<User> clickListener) {
        userList = items;
        checkInWorkersBySiteViewModel = cvm;
        listener = clickListener;
    }


    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ObjectCheckinworkersbysiteBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        User currUser = userList.get(position);
        holder.tv_userName.setText(currUser.getUserName());
        holder.tv_phoneNum.setText(currUser.getPhoneNumber());

        if(currUser.getAccidentHistory()!=""){
            holder.card.setBackgroundColor(Color.RED);
        }
        if (listener != null && listener instanceof OnTodayWorksiteContextMenuInteractionListener) {
            holder.card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((OnTodayWorksiteContextMenuInteractionListener<User>) listener).onItemClick(userList.get(holder.getAdapterPosition()));
                }
            });
            holder.card.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
                @Override
                public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo) {
                    menu.setHeaderTitle("Select Action");
                    MenuItem returnUser = menu.add(Menu.NONE, 1, 1, "Return User");
                    returnUser.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            ((OnTodayWorksiteContextMenuInteractionListener<User>) listener).onContextReturnWorksite(userList.get(holder.getAdapterPosition()));
                            return true;
                        }
                    });
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

        public ViewHolder(ObjectCheckinworkersbysiteBinding binding) {
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
