package com.gausslab.managerapp.dataSource;

public interface DataSourceCallback<Result> {
    void onComplete(Result result);
}
