package com.gausslab.managerapp.todayworksite;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.gausslab.managerapp.model.Result;
import com.gausslab.managerapp.WorksiteRepository;
import com.gausslab.managerapp.model.Worksite;

import java.util.ArrayList;
import java.util.List;

public class TodayWorkSiteViewModel extends ViewModel {
    private WorksiteRepository worksiteRepository = WorksiteRepository.getInstance();
    private MutableLiveData<Boolean> todayWorksiteListLoaded = new MutableLiveData<>(false);

    private List<Worksite> worksiteList = new ArrayList<>();

    public void loadTodayWorksite() {
        worksiteRepository.getTodayWorksite(result -> {
            if (result instanceof Result.Success) {
                worksiteList = ((Result.Success<List<Worksite>>) result).getData();
                todayWorksiteListLoaded.setValue(true);
            }
        });
    }

    public List<Worksite> getTodayWorksite() {
        return worksiteList;
    }

    public LiveData<Boolean> todayWorksiteListLoaded() {
        return todayWorksiteListLoaded;
    }

}
