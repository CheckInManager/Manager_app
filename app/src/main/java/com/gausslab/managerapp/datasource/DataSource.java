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

    void changeUserInformation(User changeUserInformation, CompletedCallback<Result<String>> callback);

    void changeNoPhoneNumberUserInformation(User changeUserInformation, CompletedCallback<Result<String>> callback);

    void addUser(User addNewUser, CompletedCallback<Result<String>> callback);

    void addGuestUser(User addNewUser, CompletedCallback<Result<String>> callback);

    void getPhoneNumberList(CompletedCallback<Result<List<String>>> callback);

    void addNotice(Notice notice, CompletedCallback<Result<String>> callback);

    void getNoticeList(ListenerCallback<Result<List<Notice>>> callback);

    void deleteNotice(String noticeName, String worksiteName, CompletedCallback<Result<String>> callback);

    void getNoticeDetailByName(String noticeName, String worksiteName, CompletedCallback<Result<Notice>> callback);

    void addAccidentHistory(AccidentHistory accidentHistory, CompletedCallback<Result<String>> callback);

    void getAccidentHistoryByUser(String phoneNumber, ListenerCallback<Result<List<AccidentHistory>>> callback);
}
