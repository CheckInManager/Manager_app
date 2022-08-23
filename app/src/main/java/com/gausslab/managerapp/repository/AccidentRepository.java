package com.gausslab.managerapp.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.gausslab.managerapp.FileService;
import com.gausslab.managerapp.datasource.CompletedCallback;
import com.gausslab.managerapp.datasource.DataSource;
import com.gausslab.managerapp.datasource.ListenerCallback;
import com.gausslab.managerapp.model.AccidentHistory;
import com.gausslab.managerapp.model.Result;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

public class AccidentRepository {
    private static volatile AccidentRepository INSTANCE = new AccidentRepository();
    protected Executor executor;
    protected Map<String, MutableLiveData<Boolean>> userAccidentHistoryLoaded = new HashMap<>();
    private DataSource dataSource;
    private FileService fileService;
    private Map<String, List<AccidentHistory>> userAccidentHistoryMap = new HashMap<>();

    public static AccidentRepository getInstance() {
        return INSTANCE;
    }

    public void addAccidentHistory(AccidentHistory accidentHistory, CompletedCallback<Result<String>> callback) {
        dataSource.addAccidentHistory(accidentHistory, callback);
    }

    public void registerAccidentHistoryListListener(String phoneNumber, ListenerCallback<List<AccidentHistory>> callback) {
        userAccidentHistoryMap.put(phoneNumber, new ArrayList<>());
        userAccidentHistoryLoaded.put(phoneNumber, new MutableLiveData<>());
        dataSource.getAccidentHistoryByUser(phoneNumber, new ListenerCallback<Result<List<AccidentHistory>>>() {
            @Override
            public void onUpdate(Result<List<AccidentHistory>> result) {
                if (result instanceof Result.Success) {
                    List<AccidentHistory> accidentHistoryList = ((Result.Success<List<AccidentHistory>>) result).getData();
                    userAccidentHistoryMap.put(phoneNumber, accidentHistoryList);
                    callback.onUpdate(accidentHistoryList);
                }
            }
        });
    }

    public void deleteAccidentHistory(final String keyValue, CompletedCallback<Result<String>>
            callback) {
        dataSource.deleteAccidentHistory(keyValue, callback);
    }

    public void changeAccidentHistory(final AccidentHistory accidentHistory,
                                      final CompletedCallback<Result<String>> callback) {
        dataSource.changeAccidentHistory(accidentHistory, callback);
    }

    public void getAccidentHistory(String key, CompletedCallback<Result<AccidentHistory>> callback) {
        dataSource.getAccidentHistoryByKey(key, callback);
    }

    public void setExecutor(Executor exec) {
        this.executor = exec;
    }

    public void setDataSource(DataSource ds) {
        this.dataSource = ds;
    }

    public void setFileService(FileService fs) {
        this.fileService = fs;
    }

}
