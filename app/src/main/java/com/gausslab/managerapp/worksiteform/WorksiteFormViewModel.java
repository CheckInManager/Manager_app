package com.gausslab.managerapp.worksiteform;

import android.graphics.drawable.Drawable;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.gausslab.managerapp.WorksiteRepository;
import com.gausslab.managerapp.model.Result;
import com.gausslab.managerapp.model.Worksite;

public class WorksiteFormViewModel extends ViewModel {

    private WorksiteRepository worksiteRepository = WorksiteRepository.getInstance();
    private MutableLiveData<Boolean> doingWork = new MutableLiveData<>(false);
    private MutableLiveData<Boolean> addWorksiteSuccess = new MutableLiveData<>(false);

    public void addWorksite(Worksite worksite) {
        worksiteRepository.addWorksite(worksite, result ->
        {
            if (result instanceof Result.Success) {
                doingWork.postValue(true);
            } else {
                doingWork.postValue(false);
            }
        });
    }

    public void createQrForWorksite(Worksite toCreate) {
        worksiteRepository.createQrForWorksite(toCreate, result ->
        {
            if (result instanceof Result.Success) {
                addWorksiteSuccess.postValue(true);
            } else {
                addWorksiteSuccess.postValue(false);
            }
        });
    }

    public LiveData<Boolean> doingWork() {
        return doingWork;
    }

    public LiveData<Boolean> addWorksiteSuccess() {
        return addWorksiteSuccess;
    }
}
