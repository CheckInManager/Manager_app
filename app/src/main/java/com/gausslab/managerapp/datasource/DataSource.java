package com.gausslab.managerapp.datasource;

import com.gausslab.managerapp.model.Result;
import com.gausslab.managerapp.model.User;
import com.gausslab.managerapp.model.Worksite;

import java.util.List;

public interface DataSource {
    void getTodayWorksiteList(String todayCal, CompletedCallback<Result<List<Worksite>>> callback);

    void addWorksite(Worksite worksite, CompletedCallback<Result<String>> callback);

    void getUserListByWorksite(String worksiteName, ListenerCallback<Result<List<User>>> callback);

    void getUserByPhoneNumber(String phoneNumber, CompletedCallback<Result<User>> callback);

    void changeUserInformation(User changeUserInformation, CompletedCallback<Result<String>> callback);

    void addUser(User addNewUser, CompletedCallback<Result<String>> callback);

    void getPhoneNumberList(CompletedCallback<Result<List<String>>> callback);

    void loadWorksiteNameList(CompletedCallback<Result<List<String>>> callback);
}
