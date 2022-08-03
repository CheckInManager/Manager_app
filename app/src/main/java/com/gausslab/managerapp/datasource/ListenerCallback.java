package com.gausslab.managerapp.datasource;

public interface ListenerCallback<T> {
    void onUpdate(T result);
}
