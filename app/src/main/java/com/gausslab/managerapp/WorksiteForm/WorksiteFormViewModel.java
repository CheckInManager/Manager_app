package com.gausslab.managerapp.WorksiteForm;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.gausslab.managerapp.UserRepository;
import com.gausslab.managerapp.model.Result;
import com.gausslab.managerapp.model.Worksite;

public class WorksiteFormViewModel extends ViewModel {

    private UserRepository userRepository = UserRepository.getInstance();
    private MutableLiveData<Boolean> addWorksiteSuccess = new MutableLiveData<>(false);

    public void addWorksite(Worksite worksite){
        userRepository.addWorksite(worksite, result->{
            if(result instanceof Result.Success){
                addWorksiteSuccess.postValue(true);
            } else{
                addWorksiteSuccess.postValue(false);
            }
        });
    }

    public void createQrForWorksite(Worksite toCreate){
        userRepository.createQrForWorksite(toCreate, result->{
           if(result instanceof Result.Success){

           } else{

           }
        });
    }


    public LiveData<Boolean> addWorksiteSuccess(){return addWorksiteSuccess;}

    public void setAddWorksiteSuccess(Boolean value){ addWorksiteSuccess.postValue(value);}
}
