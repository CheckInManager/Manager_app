package com.gausslab.managerapp.repository;

import androidx.cardview.widget.CardView;

import com.gausslab.managerapp.FileService;
import com.gausslab.managerapp.datasource.CompletedCallback;
import com.gausslab.managerapp.datasource.DataSource;
import com.gausslab.managerapp.model.AccidentHistory;
import com.gausslab.managerapp.model.Result;

import java.util.concurrent.Executor;

public class AccidentRepository {
    private static volatile AccidentRepository  INSTANCE = new AccidentRepository();
    protected Executor executor;
    private DataSource dataSource;
    private FileService fileService;

    public static AccidentRepository getInstance(){return INSTANCE;}

    public void addAccidentHistory(String userPhoneNumber, AccidentHistory accidentHistory, CompletedCallback<Result<String>> callback){
        dataSource.addAccidentHistory(userPhoneNumber, accidentHistory, callback);
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
