package com.gausslab.managerapp.datasource;

import com.gausslab.managerapp.model.Result;
import com.gausslab.managerapp.model.User;
import com.gausslab.managerapp.model.Worksite;

public interface DataSource {
    void getTodayWorksite(String todayCal,DataSourceCallback<Result> callback);

    void addWorksite(Worksite worksite, DataSourceCallback<Result> callback);

    void getDocumentsFromCollection(String collectionName, DataSourceListenerCallback<Result> callback);

    void getUserByWorksite(String worksiteName, DataSourceCallback<Result> callback);

    void getUserInformation(String phoneNumber, DataSourceCallback<Result> callback);

    void changeInformation(User changeInformation, DataSourceCallback<Result> callback);
}
