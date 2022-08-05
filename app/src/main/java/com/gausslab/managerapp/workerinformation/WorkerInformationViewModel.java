package com.gausslab.managerapp.workerinformation;

import android.graphics.drawable.Drawable;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.gausslab.managerapp.datasource.CompletedCallback;
import com.gausslab.managerapp.repository.UserRepository;
import com.gausslab.managerapp.model.Result;
import com.gausslab.managerapp.model.User;

public class WorkerInformationViewModel extends ViewModel {
    private final UserRepository userRepository = UserRepository.getInstance();
    private final MutableLiveData<Boolean> userInformationLoaded = new MutableLiveData<>(false);

    private User currUser;

    private Drawable userImage;

    public void loadUserInformation(String phoneNumber){
        userRepository.getUserByPhoneNumber(phoneNumber, result->{
           if(result instanceof Result.Success){
               currUser = ((Result.Success<User>)result).getData();
               userInformationLoaded.setValue(true);
           }
        });
    }

    public void changeInformation(User changeInformation){
        userRepository.changeUserInformation(changeInformation, result->{
           if(result instanceof Result.Success){

           }
        });
    }

    public void changeNoPhoneNumberUserInformation(User changeInformation){
        userRepository.changeNoPhoneNumberUserInformation(changeInformation, result->{
            if(result instanceof Result.Success){

            }
        });
    }

    public void loadUserImage(String phoneNumber){
        userRepository.loadUserImageDrawable(phoneNumber, new CompletedCallback<Result<Drawable>>(){
            @Override
            public void onComplete(Result<Drawable> drawableResult){
                if(drawableResult instanceof Result.Success){
                    userImage = ((Result.Success<Drawable>)drawableResult).getData();
                }else{

                }
            }
        });
    }

    public Drawable getUserImage(){return userImage;}

    public User getUserInformation(){return currUser;}

    public LiveData<Boolean> userInformationLoaded(){return userInformationLoaded;}

}
