package com.gausslab.managerapp.Repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.gausslab.managerapp.FileService;
import com.gausslab.managerapp.datasource.DataSource;
import com.gausslab.managerapp.datasource.DataSourceCallback;
import com.gausslab.managerapp.datasource.DataSourceListenerCallback;
import com.gausslab.managerapp.model.Result;
import com.gausslab.managerapp.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

public class UserRepository
{

    private static volatile UserRepository INSTANCE = new UserRepository();
    protected Executor executor;
    private DataSource dataSource;
    private FileService fileService;
    private Map<String, List<User>> worksiteUsersMap = new HashMap<>();

    private Map<String, MutableLiveData<Boolean>> worksiteUsersLoaded = new HashMap<>();

    public static UserRepository getInstance()
    {
        return INSTANCE;
    }

    public List<User> getUsersByWorksite(final String worksiteName)
    {
        return worksiteUsersMap.get(worksiteName);
    }

    public void registerWorksiteUserListListener(String worksiteName)
    {
        if (worksiteUsersMap.containsKey(worksiteName))
            return;

        worksiteUsersMap.put(worksiteName, new ArrayList<>());
        worksiteUsersLoaded.put(worksiteName, new MutableLiveData<>());
        dataSource.getUsersByWorksite(worksiteName, new DataSourceListenerCallback<Result<List<User>>>()
        {
            @Override
            public void onUpdate(Result<List<User>> result)
            {
                if (result instanceof Result.Success)
                {
                    worksiteUsersMap.put(worksiteName, ((Result.Success<List<User>>) result).getData());
                    worksiteUsersLoaded.get(worksiteName).postValue(true);
                }
                else
                {

                }
            }
        });
    }

    public void getUserByPhoneNumber(final String phoneNumber, final UserRepositoryCallback callback)
    {
        dataSource.getUserByPhoneNumber(phoneNumber, callback::onComplete);
    }

    public void changeInformation(final User changeInformation, final UserRepositoryCallback callback)
    {
        dataSource.changeInformation(changeInformation, callback::onComplete);
    }

    public void setExecutor(Executor exec)
    {
        this.executor = exec;
    }

    public void setDataSource(DataSource ds)
    {
        this.dataSource = ds;
    }

    public void setFileService(FileService fs)
    {
        this.fileService = fs;
    }

    public LiveData<Boolean> isUserListLoadedForWorksite(String worksite)
    {
        return worksiteUsersLoaded.get(worksite);
    }

    public interface UserRepositoryCallback<Result>
    {
        void onComplete(Result result);
    }

    public interface UserRepositoryListenerCallback<Result>
    {

    }
}
