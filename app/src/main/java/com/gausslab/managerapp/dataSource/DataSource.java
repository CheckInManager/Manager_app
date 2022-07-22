package com.gausslab.managerapp.dataSource;

import com.gausslab.managerapp.Result;
import com.gausslab.managerapp.Worksite;

public interface DataSource {
    void getTodayWorksite(DataSourceCallback<Result> callback);
    void addWorksite(Worksite worksite, DataSourceCallback<Result> callback);
}
