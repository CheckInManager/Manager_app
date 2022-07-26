package com.gausslab.managerapp.datasource;

public interface DataSourceCallback<T> {
    void onComplete(T result);
}
