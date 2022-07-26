package com.gausslab.managerapp.checkinworkerbysite;

import androidx.lifecycle.ViewModel;

import com.gausslab.managerapp.model.User;

import java.util.ArrayList;
import java.util.List;

public class CheckInWorkersBySiteViewModel extends ViewModel {

    private List<User> userList = new ArrayList<>();
    public List<User> getUserInformationList(){
        return userList;
    }
}
