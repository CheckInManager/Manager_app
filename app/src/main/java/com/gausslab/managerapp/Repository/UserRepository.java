package com.gausslab.managerapp.Repository;

import com.gausslab.managerapp.FileService;
import com.gausslab.managerapp.datasource.DataSource;
import com.gausslab.managerapp.model.User;

import java.util.concurrent.Executor;

public class UserRepository {

    private static volatile UserRepository INSTANCE = new UserRepository();
    private DataSource dataSource;
    private FileService fileService;
    protected Executor executor;

    public static UserRepository getInstance() {
        return INSTANCE;
    }

    public void getUserByWorksite(final String worksiteName, final UserRepositoryCallback callback){
        dataSource.getUserByWorksite(worksiteName,callback::onComplete);
    }

    public void getUserInformation(final String phoneNumber, final UserRepositoryCallback callback){
        dataSource.getUserInformation(phoneNumber, callback::onComplete);
    }

    public void changeInformation(final User changeInformation, final UserRepositoryCallback callback){
        dataSource.changeInformation(changeInformation,callback::onComplete);
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


    public interface UserRepositoryCallback<Result> {
        void onComplete(Result result);
    }
}
