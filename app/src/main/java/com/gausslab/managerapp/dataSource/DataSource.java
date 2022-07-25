package com.gausslab.managerapp.dataSource;

import com.gausslab.managerapp.model.Result;
import com.gausslab.managerapp.model.Worksite;

public interface DataSource {
    void getTodayWorksite(DataSourceCallback<Result> callback);

    void addWorksite(Worksite worksite, DataSourceCallback<Result> callback);

    void getDocumentsFromCollection(String collectionName, DataSourceListenerCallback<Result> callback);
}
