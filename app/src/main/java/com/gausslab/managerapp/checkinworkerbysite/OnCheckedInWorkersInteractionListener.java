package com.gausslab.managerapp.checkinworkerbysite;

import com.gausslab.managerapp.model.Notice;
import com.gausslab.managerapp.model.User;
import com.gausslab.managerapp.todayworksite.OnItemInteractionListener;

public interface OnCheckedInWorkersInteractionListener extends OnItemInteractionListener<User> {
    void onClick(User obj);

    void onDelete(User obj);
}
