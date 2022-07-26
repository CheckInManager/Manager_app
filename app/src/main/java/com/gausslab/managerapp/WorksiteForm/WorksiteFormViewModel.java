package com.gausslab.managerapp.WorksiteForm;

import android.graphics.drawable.Drawable;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.gausslab.managerapp.UserRepository;
import com.gausslab.managerapp.model.Result;
import com.gausslab.managerapp.model.Worksite;

public class WorksiteFormViewModel extends ViewModel {

    private UserRepository userRepository = UserRepository.getInstance();
    private MutableLiveData<Boolean> doingWork = new MutableLiveData<>(false);
    private MutableLiveData<Boolean> addWorksiteSuccess = new MutableLiveData<>(false);

    private final MutableLiveData<Boolean> dataLoaded = new MutableLiveData<>(false);
    private LiveData<Boolean> qrLoaded;

    private Worksite currWorksite;

    public void addWorksite(Worksite worksite) {
        userRepository.addWorksite(worksite, result -> {
            if (result instanceof Result.Success) {
                doingWork.postValue(true);
            } else {
                doingWork.postValue(false);
            }
        });
    }

    public void createQrForWorksite(Worksite toCreate) {
        userRepository.createQrForWorksite(toCreate, result -> {
            if (result instanceof Result.Success) {
                addWorksiteSuccess.postValue(true);
            } else {
                addWorksiteSuccess.postValue(false);
            }
        });
    }
    public void setCurrWorksite(Worksite w){
        currWorksite = w;
        qrLoaded = userRepository.isQrLoaded(currWorksite.getWorkName());
    }

    public void setCurrWorksite(String worksiteName){
        setCurrWorksite(userRepository.getWorksite(worksiteName));
    }

    public Drawable getQrDrawable(){
        return userRepository.getQrDrawable(currWorksite.getWorkName());
    }

    public void loadDrawables(){
        userRepository.loadQrDrawableForDevice(currWorksite.getWorkName(), new UserRepository.UserRepositoryCallback<Result>(){
           @Override
           public void onComplete(Result result){

           }
        });
    }

    public void loadWorksiteList(){
        userRepository.loadWorksiteList();
        dataLoaded.postValue(true);
    }

    public void getWorksiteList(){
        userRepository.getWorksiteList();
    }


    public LiveData<Boolean> doingWork() {
        return doingWork;
    }

    public LiveData<Boolean> addWorksiteSuccess() {
        return addWorksiteSuccess;
    }

    public LiveData<Boolean> isQrLoaded(){return  qrLoaded;}

    public LiveData<Boolean> isDataLoaded() {
        return dataLoaded;
    }
}
