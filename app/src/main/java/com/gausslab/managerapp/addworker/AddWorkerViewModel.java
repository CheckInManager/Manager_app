package com.gausslab.managerapp.addworker;

import android.graphics.Bitmap;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.gausslab.managerapp.App;
import com.gausslab.managerapp.FileService;
import com.gausslab.managerapp.model.Worksite;
import com.gausslab.managerapp.repository.UserRepository;
import com.gausslab.managerapp.repository.WorksiteRepository;
import com.gausslab.managerapp.model.Result;
import com.gausslab.managerapp.model.User;
import com.google.firebase.Timestamp;

import java.util.ArrayList;
import java.util.List;

public class AddWorkerViewModel extends ViewModel {
    private final UserRepository userRepository = UserRepository.getInstance();
    private final WorksiteRepository worksiteRepository = WorksiteRepository.getInstance();
    private MutableLiveData<Boolean> openWorksiteListLoaded = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> changedSpinnerStringLoaded = new MutableLiveData<>(false);
    private List<String> phoneNumberList = new ArrayList<>();
    private List<Worksite> worksiteList = new ArrayList<>();
    private String worksiteKeyValue;

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
        userRepository.addOrUpdateUser(userToAdd, result -> {
            if (result instanceof Result.Success) {

            } else {

            }
        });
    }

    public void addGuestUser(User userToAdd) {
        userRepository.addOrUpdateUser(userToAdd, result -> {
            if (result instanceof Result.Success) {

            } else {

            }
        });
    }

    public void saveBitmapToMediaStore(Bitmap bm) {
        FileService fileService = App.getFileService();
        fileService.saveBitmapToMediaStore("userImage_" + Timestamp.now().getSeconds(), bm, new FileService.FileServiceCallback<Result<String>>() {
            @Override
            public void onComplete(Result result) {
            }
        });
    }

    public void saveUserImage(User user, Bitmap userImage) {
        userRepository.saveUserImage(user, userImage, result -> {
            if (result instanceof Result.Success) {

            } else {

            }
        });
    }

    public void saveNoPhoneNumberUserImage(User user, Bitmap userImage) {
        userRepository.saveNoPhoneNumberUserImage(user, userImage, result -> {
            if (result instanceof Result.Success) {

            } else {

            }
        });
    }

    public void loadOpenWorksite(String todayCal) {
        worksiteRepository.getTodayWorksite(todayCal, result -> {
            if (result instanceof Result.Success) {
                worksiteList = ((Result.Success<List<Worksite>>) result).getData();
                openWorksiteListLoaded.setValue(true);
            }
        });
    }

    public void changeSpinnerStringToKeyValue(String worksiteName) {
        worksiteRepository.changeSpinnerStringToKeyValue(worksiteName, result -> {
            if (result instanceof Result.Success) {
                worksiteKeyValue = ((Result.Success<String>) result).getData();
                changedSpinnerStringLoaded.postValue(true);
            } else {
                changedSpinnerStringLoaded.postValue(false);
            }
        });
    }

    public String getWorksiteKeyValue() {
        return worksiteKeyValue;
    }

    public List<Worksite> getOpenWorksite() {
        return worksiteList;
    }

    public LiveData<Boolean> openWorksiteListLoaded() {
        return openWorksiteListLoaded;
    }

    public LiveData<Boolean> isChangedSpinnerString() {
        return changedSpinnerStringLoaded;
    }
}
