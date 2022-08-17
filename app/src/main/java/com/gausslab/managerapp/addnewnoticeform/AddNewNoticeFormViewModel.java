package com.gausslab.managerapp.addnewnoticeform;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.gausslab.managerapp.model.Notice;
import com.gausslab.managerapp.model.Result;
import com.gausslab.managerapp.model.Worksite;
import com.gausslab.managerapp.repository.WorksiteRepository;

import java.util.ArrayList;
import java.util.List;

public class AddNewNoticeFormViewModel extends ViewModel {
    private WorksiteRepository worksiteRepository = WorksiteRepository.getInstance();
    private MutableLiveData<Boolean> addNoticeFormSuccess = new MutableLiveData<>(false);
    private MutableLiveData<Boolean> openWorksiteListLoaded = new MutableLiveData<>(false);

    private List<Worksite> worksiteList = new ArrayList<>();

    public void addNotice(Notice notice) {
        worksiteRepository.addNotice(notice, result ->
        {
            if (result instanceof Result.Success) {
                addNoticeFormSuccess.postValue(true);
            } else {
                addNoticeFormSuccess.postValue(false);
            }
        });
    }

    public void loadOpenWorksite(String todayCal) {
        worksiteRepository.getTodayWorksite(todayCal, result ->
        {
            if (result instanceof Result.Success) {
                worksiteList = ((Result.Success<List<Worksite>>) result).getData();
                openWorksiteListLoaded.setValue(true);
            }
        });
    }

    public List<Worksite> getOpenWorksite() {
        return worksiteList;
    }

    public LiveData<Boolean> isAddNoticeFormSuccess() {
        return addNoticeFormSuccess;
    }

    public LiveData<Boolean> openWorksiteListLoaded() {
        return openWorksiteListLoaded;
    }

}
