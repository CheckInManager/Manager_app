package com.gausslab.managerapp.datasource;

import com.gausslab.managerapp.model.AccidentHistory;
import com.gausslab.managerapp.model.Notice;
import com.gausslab.managerapp.model.Result;
import com.gausslab.managerapp.model.User;
import com.gausslab.managerapp.model.Worksite;

import java.util.List;

public interface DataSource {
    void getTodayWorksiteList(String todayCal, CompletedCallback<Result<List<Worksite>>> callback);

    void addWorksite(Worksite worksite, CompletedCallback<Result<String>> callback);

    void getUserListByWorksite(String worksiteName, ListenerCallback<Result<List<User>>> callback);

    void getUserByPhoneNumber(String phoneNumber, CompletedCallback<Result<User>> callback);

    void noPhoneNumberGetUser(String userName, CompletedCallback<Result<User>> callback);

    void addOrUpdateUser(User user, CompletedCallback<Result<String>> callback);

    void getPhoneNumberList(CompletedCallback<Result<List<String>>> callback);

    void addNotice(Notice notice, CompletedCallback<Result<String>> callback);

    void getNoticeList(ListenerCallback<Result<List<Notice>>> callback);

    void deleteNotice(String keyValue, CompletedCallback<Result<String>> callback);

    void getNoticeDetailByName(String keyValue, CompletedCallback<Result<Notice>> callback);

    void addAccidentHistory(AccidentHistory accidentHistory, CompletedCallback<Result<String>> callback);

    void getAccidentHistoryByUser(String phoneNumber, ListenerCallback<Result<List<AccidentHistory>>> callback);

    void getAccidentHistoryByKey(String key, CompletedCallback<Result<AccidentHistory>> callback);

    void deleteAccidentHistory(String keyValue, CompletedCallback<Result<String>> callback);

    void changeAccidentHistory(AccidentHistory accidentHistory, CompletedCallback<Result<String>> callback);

    void changeNotice(Notice notice, CompletedCallback<Result<String>> callback);

    void getWorksiteByKey(String key, CompletedCallback<Result<Worksite>> callback);

    void changeSpinnerStringToKeyValue(String worksiteName, CompletedCallback<Result<String>> callback);

    void getAllWorksite(CompletedCallback<Result<List<Worksite>>> callback);

    void deleteUser(User toRemove, CompletedCallback<Result<String>> callback);

}
