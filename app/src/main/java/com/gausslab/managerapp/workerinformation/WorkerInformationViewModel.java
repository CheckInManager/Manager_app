package com.gausslab.managerapp.workerinformation;

import androidx.lifecycle.ViewModel;

import com.gausslab.managerapp.Repository.UserRepository;

public class WorkerInformationViewModel extends ViewModel {
    private final UserRepository userRepository = UserRepository.getInstance();
}
