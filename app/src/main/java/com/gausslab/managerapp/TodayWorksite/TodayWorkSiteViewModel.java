package com.gausslab.managerapp.TodayWorksite;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.gausslab.managerapp.model.Result;
import com.gausslab.managerapp.UserRepository;
import com.gausslab.managerapp.model.Worksite;

import java.util.ArrayList;
import java.util.List;

public class TodayWorkSiteViewModel extends ViewModel {
    private UserRepository userRepository = UserRepository.getInstance();
    private MutableLiveData<Boolean> todayWorksiteListLoaded = new MutableLiveData<>(false);

    private List<Worksite> worksiteList = new ArrayList<>();

    public void loadTodayWorksite() {
        userRepository.getTodayWorksite(result -> {
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
