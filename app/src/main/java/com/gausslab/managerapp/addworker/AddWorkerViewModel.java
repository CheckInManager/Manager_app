package com.gausslab.managerapp.addworker;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.gausslab.managerapp.Repository.UserRepository;
import com.gausslab.managerapp.Repository.WorksiteRepository;
import com.gausslab.managerapp.model.Result;
import com.gausslab.managerapp.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AddWorkerViewModel extends ViewModel {
    private final UserRepository userRepository = UserRepository.getInstance();
    private final WorksiteRepository worksiteRepository = WorksiteRepository.getInstance();
    private MutableLiveData<Boolean> isWorksiteNameList = new MutableLiveData<>(false);
    private List<String> phoneNumberList = new ArrayList<>();
    private List<String> worksiteNameList = new ArrayList<>();

    public void loadPhoneNumberList() {
        userRepository.loadPhoneNumberList(result -> {
            if (result instanceof Result.Success) {
                phoneNumberList = ((Result.Success<List<String>>) result).getData();
            } else {

            }
        });
    }

    public Boolean checkPhoneNumber(String phoneNumber) {
        if (phoneNumberList.contains(phoneNumber)) {
            return false;
        } else {
            return true;
        }
    }

    public void addUser(User userToAdd) {
        userRepository.addUser(userToAdd, result -> {
            if (result instanceof Result.Success) {

            } else {

            }
        });
    }

    public void loadWorksiteNameList() {
        worksiteRepository.loadWorksiteNameList(result -> {
            if (result instanceof Result.Success) {
                worksiteNameList = ((Result.Success<List<String>>) result).getData();
                isWorksiteNameList.postValue(true);
            } else {

            }
        });
    }

    public List<String> getWorksiteNameList() {
        return worksiteNameList;
    }

    public LiveData<Boolean> isWorksiteNameList() {
        return isWorksiteNameList;
    }
}
