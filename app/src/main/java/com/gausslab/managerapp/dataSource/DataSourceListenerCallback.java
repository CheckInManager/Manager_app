package com.gausslab.managerapp.dataSource;

public interface DataSourceListenerCallback<T>
{
    void onUpdate(T result);
}
