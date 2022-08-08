package com.gausslab.managerapp.checkinworkerbysite;

import android.graphics.drawable.Drawable;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.gausslab.managerapp.datasource.CompletedCallback;
import com.gausslab.managerapp.model.Result;
import com.gausslab.managerapp.model.Worksite;
import com.gausslab.managerapp.repository.UserRepository;
import com.gausslab.managerapp.model.User;
import com.gausslab.managerapp.repository.WorksiteRepository;

import java.util.List;

public class CheckedInWorkersBySiteViewModel extends ViewModel {
    private final UserRepository userRepository = UserRepository.getInstance();
    private final WorksiteRepository worksiteRepository = WorksiteRepository.getInstance();
    private MutableLiveData<Boolean> isQrLoaded = new MutableLiveData<>(false);
    private String myWorksiteName;
    private Worksite currWorksite;
    private Drawable qrImage;

    public void loadUserListByWorksite(String worksiteName) {
        myWorksiteName = worksiteName;
        userRepository.registerWorksiteUserListListener(worksiteName);
    }

    public void setWorksite(String worksiteName, String worksiteQrImagePath) {
        currWorksite = worksiteRepository.getWorksite(worksiteName);

        worksiteRepository.loadQrDrawableForWorksite(worksiteQrImagePath, new CompletedCallback<Result<Drawable>>() {
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

    public List<User> getUserList() {
        return userRepository.getUserListByWorksite(myWorksiteName);
    }

    public LiveData<Boolean> isUserListLoaded() {
        return userRepository.isUserListLoadedForWorksite(myWorksiteName);
    }

    public LiveData<Boolean> isQrImageLoaded() {
        return isQrLoaded;
    }

    public Drawable getQrImage() {
        return qrImage;
    }
}
