package com.gausslab.managerapp.todayworksite;

public interface OnTodayWorksiteContextMenuInteractionListener<T> extends OnItemInteractionListener<T>
{
    void onItemClick(T obj);

    void onContextReturnWorksite(T obj);
}
