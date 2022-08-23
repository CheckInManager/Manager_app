package com.gausslab.managerapp.workerinformation;

import com.gausslab.managerapp.model.AccidentHistory;
import com.gausslab.managerapp.todayworksite.OnItemInteractionListener;

interface OnAccidentHistoryInteractionListener extends OnItemInteractionListener<AccidentHistory> {
    void onClick(AccidentHistory obj);

    void onDelete(AccidentHistory obj);
}
