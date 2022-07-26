package com.gausslab.managerapp.datasource;

public interface DataSourceListenerCallback<T>
{
    void onUpdate(T result);
}
