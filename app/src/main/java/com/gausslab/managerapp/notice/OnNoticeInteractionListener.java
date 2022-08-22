package com.gausslab.managerapp.notice;

import com.gausslab.managerapp.model.Notice;
import com.gausslab.managerapp.todayworksite.OnItemInteractionListener;

public interface OnNoticeInteractionListener extends OnItemInteractionListener<Notice> {
    void onClick(Notice obj);

    void onDelete(Notice obj);
}
