package com.gausslab.managerapp.datasource;

import com.gausslab.managerapp.model.Result;
import com.gausslab.managerapp.model.User;
import com.gausslab.managerapp.model.Worksite;

import java.util.List;

public interface DataSource {
    void getTodayWorksiteList(String todayCal, DataSourceCallback<Result<List<Worksite>>> callback);

    void addWorksite(Worksite worksite, DataSourceCallback<Result> callback);

    void getDocumentsFromCollection(String collectionName, DataSourceListenerCallback<Result> callback);

    void getUsersByWorksite(String worksiteName, DataSourceListenerCallback<Result<List<User>>> callback);

    void getUserByPhoneNumber(String phoneNumber, DataSourceCallback<Result<User>> callback);

    void changeInformation(User changeInformation, DataSourceCallback<Result> callback);
}
