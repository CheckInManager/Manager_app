package com.gausslab.managerapp;

import com.gausslab.managerapp.dataSource.DataSource;

public class UserRepository {
    private static volatile UserRepository INSTANCE = new UserRepository();
    private DataSource dataSource;
    public  static UserRepository getInstance(){return INSTANCE;}

    public void getTodayWorksite(final UserRepositoryCallback callback){
        dataSource.getTodayWorksite(callback::onComplete);
    }

    public void addWorksite(final Worksite worksite, UserRepositoryCallback<Result> callback){
        dataSource.addWorksite(worksite, callback::onComplete);
    }

    public void setDataSource(DataSource ds){this.dataSource = ds;}

    public interface UserRepositoryCallback<Result>{
        void onComplete(Result result);
    }
}
