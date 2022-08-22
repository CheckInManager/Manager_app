package com.gausslab.managerapp.notice;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.gausslab.managerapp.databinding.ObjectNoticeBinding;
import com.gausslab.managerapp.model.Notice;
import com.gausslab.managerapp.todayworksite.OnItemInteractionListener;
import com.gausslab.managerapp.todayworksite.OnTodayWorksiteContextMenuInteractionListener;

import java.util.List;

public class NoticeRecyclerViewAdapter extends RecyclerView.Adapter<NoticeRecyclerViewAdapter.ViewHolder> {
    private List<Notice> noticeList;
    private OnNoticeInteractionListener listener;

    public NoticeRecyclerViewAdapter(List<Notice> items, OnNoticeInteractionListener clickListener) {
        noticeList = items;
        listener = clickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ObjectNoticeBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Notice currNotice = noticeList.get(position);
        holder.tv_noticeName.setText(currNotice.getNoticeName());
        holder.tv_worksiteName.setText(currNotice.getWorksiteName());
        holder.tv_time.setText(currNotice.getTime());
        holder.bt_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onDelete(currNotice);
            }
        });
        holder.cv_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClick(noticeList.get(holder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return noticeList.size();
    }

    public void setNoticeList(List<Notice> newNoticeList) {
        noticeList = newNoticeList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final CardView cv_card;
        public final TextView tv_noticeName;
        public final TextView tv_worksiteName;
        public final Button bt_delete;
        public final TextView tv_time;

        public ViewHolder(ObjectNoticeBinding binding) {
            super(binding.getRoot());
            cv_card = binding.objNoticeCard;
            tv_noticeName = binding.objNoticeTvNoticeName;
            tv_worksiteName = binding.objNoticeTvWorksiteName;
            bt_delete = binding.objNoticeBtDelete;
            tv_time = binding.objNoticeTvTime;
        }

        @Override
        public String toString() {
            return super.toString() + "";
        }
    }
}
