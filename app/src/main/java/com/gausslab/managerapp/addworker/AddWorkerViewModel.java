package com.gausslab.managerapp.addworker;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.gausslab.managerapp.Repository.UserRepository;
import com.gausslab.managerapp.model.Result;
import com.gausslab.managerapp.model.User;

import java.util.ArrayList;
import java.util.List;

public class AddWorkerViewModel extends ViewModel {
    private final UserRepository userRepository = UserRepository.getInstance();
    private MutableLiveData<Boolean> phoneNumberLoaded = new MutableLiveData<>(false);
    private List<String> phoneNumberList= new ArrayList<>();

    public void loadPhoneNumberList(){
        userRepository.loadPhoneNumberList(result->{
            if(result instanceof Result.Success){
                phoneNumberList = ((Result.Success<List<String>>)result).getData();
                phoneNumberLoaded.postValue(true);
            }else{
                phoneNumberLoaded.postValue(false);
            }
        });
    }

    public Boolean checkPhoneNumber(String phoneNumber){
        if(phoneNumberList.contains(phoneNumber)){
            return false;
        }else{
            return true;
        }
    }

    public void addUser(User addNewUser) {
        userRepository.addUser(addNewUser, result -> {
            if (result instanceof Result.Success) {

            } else {

            }
        });
    }

    public LiveData<Boolean> isPhoneNumberLoaded(){return phoneNumberLoaded;}
}
