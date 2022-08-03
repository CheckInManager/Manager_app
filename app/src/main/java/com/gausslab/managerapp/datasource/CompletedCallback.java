package com.gausslab.managerapp.datasource;

public interface CompletedCallback<T> {
    void onComplete(T result);
}
