package com.gausslab.managerapp.workerinformation;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.gausslab.managerapp.Repository.UserRepository;
import com.gausslab.managerapp.model.Result;
import com.gausslab.managerapp.model.User;

public class WorkerInformationViewModel extends ViewModel {
    private final UserRepository userRepository = UserRepository.getInstance();
    private MutableLiveData<Boolean> userInformationLoaded = new MutableLiveData<>(false);

    private User currUser;

    public void loadUserInformation(String phoneNumber){
        userRepository.getUserByPhoneNumber(phoneNumber, result->{
           if(result instanceof Result.Success){
               currUser = ((Result.Success<User>)result).getData();
               userInformationLoaded.setValue(true);
           }
        });
    }

    public void changeInformation(User changeInformation){
        userRepository.changeInformation(changeInformation,result->{
           if(result instanceof Result.Success){

           }
        });
    }

    public User getUserInformation(){return currUser;}

    public LiveData<Boolean> userInformationLoaded(){return userInformationLoaded;}

}
