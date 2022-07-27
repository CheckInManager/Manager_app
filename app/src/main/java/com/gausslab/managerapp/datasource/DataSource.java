package com.gausslab.managerapp.datasource;

import com.gausslab.managerapp.model.Result;
import com.gausslab.managerapp.model.Worksite;

public interface DataSource {
    void getTodayWorksite(DataSourceCallback<Result> callback);

    void addWorksite(Worksite worksite, DataSourceCallback<Result> callback);

    void getDocumentsFromCollection(String collectionName, DataSourceListenerCallback<Result> callback);
}