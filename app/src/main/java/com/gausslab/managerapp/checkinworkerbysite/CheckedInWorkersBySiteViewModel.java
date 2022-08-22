package com.gausslab.managerapp.checkinworkerbysite;

import android.graphics.drawable.Drawable;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.gausslab.managerapp.datasource.CompletedCallback;
import com.gausslab.managerapp.datasource.ListenerCallback;
import com.gausslab.managerapp.model.AccidentHistory;
import com.gausslab.managerapp.model.Result;
import com.gausslab.managerapp.model.Worksite;
import com.gausslab.managerapp.repository.AccidentRepository;
import com.gausslab.managerapp.repository.UserRepository;
import com.gausslab.managerapp.model.User;
import com.gausslab.managerapp.repository.WorksiteRepository;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CheckedInWorkersBySiteViewModel extends ViewModel {
    private final UserRepository userRepository = UserRepository.getInstance();
    private final WorksiteRepository worksiteRepository = WorksiteRepository.getInstance();
    private MutableLiveData<Boolean> isQrLoaded = new MutableLiveData<>(false);
    private final MutableLiveData<List<User>> userList = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isWorksiteLoaded = new MutableLiveData<>(false);
    private Drawable qrImage;
    private Worksite worksite;

    public void loadUserListByWorksite(String keyValue) {
        userRepository.registerWorksiteUserListListener(keyValue, new ListenerCallback<List<User>>() {
            @Override
            public void onUpdate(List<User> result) {
                userList.postValue(result);
            }
        });
    }

    public void setWorksite(String keyValue) {
        worksiteRepository.loadQrDrawableForWorksite(keyValue, new CompletedCallback<Result<Drawable>>() {
            @Override
            public void onComplete(Result<Drawable> drawableResult) {
                if (drawableResult instanceof Result.Success) {
                    qrImage = ((Result.Success<Drawable>) drawableResult).getData();
                    isQrLoaded.postValue(true);
                } else {
                    //오류 보여주기
                }
            }
        });
    }

    public void loadWorksite(String key) {
        loadUserListByWorksite(key);
        worksiteRepository.getWorksiteByKey(key, new CompletedCallback<Result<Worksite>>() {
            @Override
            public void onComplete(Result<Worksite> result) {
                if (result instanceof Result.Success) {
                    worksite = ((Result.Success<Worksite>) result).getData();
                    isWorksiteLoaded.postValue(true);
                } else {
                    isWorksiteLoaded.postValue(false);
                }
            }
        });
    }

    public File getQrFile(String key) {
        return worksiteRepository.getQrFileForWorksite(key);
    }

    public String getWorksiteName() {
        return worksite.getWorksiteName();
    }

    public LiveData<List<User>> getUserList() {
        if (userList.getValue() == null) {
            userList.postValue(new ArrayList<>());
        }
        return userList;
    }

    public void deleteUser(User toRemove){
        userRepository.deleteUser(toRemove, result->{
           if(result instanceof Result.Success){

           }
        });
    }

    public LiveData<Boolean> isQrImageLoaded() {
        return isQrLoaded;
    }

    public Drawable getQrImage() {
        return qrImage;
    }

    public LiveData<Boolean> isWorksiteLoaded() {
        return isWorksiteLoaded;
    }

}
