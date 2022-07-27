package com.gausslab.managerapp.checkinworkerbysite;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.gausslab.managerapp.UserRepository;
import com.gausslab.managerapp.WorksiteRepository;
import com.gausslab.managerapp.model.Result;
import com.gausslab.managerapp.model.User;
import com.gausslab.managerapp.model.Worksite;

import java.util.ArrayList;
import java.util.List;

public class CheckInWorkersBySiteViewModel extends ViewModel {
    private final UserRepository userRepository = UserRepository.getInstance();
    private MutableLiveData<Boolean> userListLoaded = new MutableLiveData<>(false);

    private List<User> userList = new ArrayList<>();

    public void loadUserByWorksite(String worksiteName) {
        userRepository.getUserByWorksite(worksiteName,result -> {
            if (result instanceof Result.Success) {
                userList = ((Result.Success<List<User>>) result).getData();
                userListLoaded.setValue(true);
            }
        });
    }

    public List<User> getUserByWorksite() {
        return userList;
    }

    public LiveData<Boolean> userListLoaded() {
        return userListLoaded;
    }

}
