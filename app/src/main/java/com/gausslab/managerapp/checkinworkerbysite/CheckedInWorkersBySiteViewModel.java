package com.gausslab.managerapp.checkinworkerbysite;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.gausslab.managerapp.Repository.UserRepository;
import com.gausslab.managerapp.model.User;

import java.util.List;

public class CheckedInWorkersBySiteViewModel extends ViewModel
{
    private final UserRepository userRepository = UserRepository.getInstance();
    private String myWorksiteName;

    public void loadUserListByWorksite(String worksiteName)
    {
        myWorksiteName = worksiteName;
        userRepository.registerWorksiteUserListListener(worksiteName);
    }

    public List<User> getUserList()
    {
        return userRepository.getUsersByWorksite(myWorksiteName);
    }

    public LiveData<Boolean> isUserListLoaded()
    {
        return userRepository.isUserListLoadedForWorksite(myWorksiteName);
    }
}
