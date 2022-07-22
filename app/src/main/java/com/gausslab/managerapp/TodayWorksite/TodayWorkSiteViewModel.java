package com.gausslab.managerapp.TodayWorksite;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.gausslab.managerapp.Result;
import com.gausslab.managerapp.UserRepository;
import com.gausslab.managerapp.Worksite;

import java.util.ArrayList;
import java.util.List;

public class TodayWorkSiteViewModel extends ViewModel {
    private UserRepository userRepository = UserRepository.getInstance();
    private MutableLiveData<Boolean> todayWorksiteListLoaded = new MutableLiveData<>(false);
    private MutableLiveData<Boolean> addWorksiteSuccess = new MutableLiveData<>(false);

    private List<Worksite> worksiteList = new ArrayList<>();

    public void loadTodayWorksite(){
        userRepository.getTodayWorksite(result->{
           if(result instanceof Result.Success){
               worksiteList = ((Result.Success<List<Worksite>>)result).getData();
               todayWorksiteListLoaded.setValue(true);
           }
        });
    }

    public void addWorksite(Worksite worksite){
        userRepository.addWorksite(worksite, result->{
           if(result instanceof Result.Success){
               addWorksiteSuccess.postValue(true);
           } else{
               addWorksiteSuccess.postValue(false);
           }
        });
    }

    public List<Worksite> getTodayWorksite(){return worksiteList;}

    public LiveData<Boolean> todayWorksiteListLoaded(){
        return todayWorksiteListLoaded;
    }

    public LiveData<Boolean> addWorksiteSuccess(){return addWorksiteSuccess;}

    public void setAddWorksiteSuccess(Boolean value){ addWorksiteSuccess.postValue(value);}
}
