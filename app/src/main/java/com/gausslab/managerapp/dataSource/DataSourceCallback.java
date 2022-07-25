package com.gausslab.managerapp.dataSource;

public interface DataSourceCallback<T> {
    void onComplete(T result);
}
