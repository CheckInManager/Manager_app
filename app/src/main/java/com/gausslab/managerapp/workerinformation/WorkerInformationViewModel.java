package com.gausslab.managerapp.workerinformation;

import android.graphics.drawable.Drawable;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.gausslab.managerapp.Event;
import com.gausslab.managerapp.datasource.CompletedCallback;
import com.gausslab.managerapp.datasource.ListenerCallback;
import com.gausslab.managerapp.model.AccidentHistory;
import com.gausslab.managerapp.model.Result;
import com.gausslab.managerapp.model.User;
import com.gausslab.managerapp.repository.AccidentRepository;
import com.gausslab.managerapp.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

public class WorkerInformationViewModel extends ViewModel
{
    private final UserRepository userRepository = UserRepository.getInstance();
    private final AccidentRepository accidentRepository = AccidentRepository.getInstance();

    private final MutableLiveData<Boolean> userInformationLoaded = new MutableLiveData<>(false);
    private final MutableLiveData<List<AccidentHistory>> accidentHistoryList = new MutableLiveData<>();
    private final MutableLiveData<Event<Boolean>> addAccidentHistorySuccess = new MutableLiveData<Event<Boolean>>();

    private User currUser;
    private Drawable userImage;

    private List<AccidentHistory> deletedAccidentHistoryList;
    private List<AccidentHistory> addedAccidentHistoryList;

    public void saveUser()
    {
        userRepository.addOrUpdateUser(currUser, new CompletedCallback<Result<String>>()
        {
            @Override
            public void onComplete(Result<String> result)
            {

            }
        });
    }

    public void addAccidentHistory(AccidentHistory toAdd)
    {
        List<AccidentHistory> updatedList = new ArrayList<>(accidentHistoryList.getValue());
        updatedList.add(toAdd);
        accidentHistoryList.postValue(updatedList);
        addAccidentHistorySuccess.postValue(new Event<>(true));
    }

    public void updateMemoText(String newMemoText)
    {
        currUser.setMemo(newMemoText);
    }

    public void deleteAccidentHistory(AccidentHistory toRemove)
    {
        List<AccidentHistory> updatedList = new ArrayList<>(accidentHistoryList.getValue());
        updatedList.remove(toRemove);
        accidentHistoryList.postValue(updatedList);
    }

    public void loadAllUserInformation(String phoneNumberOrUserName, boolean userHasPhoneNumber)
    {
        //로딩 안해도되는 조건
        if (currUser != null)
        {
            if (userHasPhoneNumber && currUser.getPhoneNumber().equals(phoneNumberOrUserName))
                return;
            else if (!userHasPhoneNumber && currUser.getUserName().equals(phoneNumberOrUserName))
                return;
        }

        if (userHasPhoneNumber)
            loadUserInformation(phoneNumberOrUserName);
        else
            loadNoPhoneNumberUserInformation(phoneNumberOrUserName);
        loadUserImage(phoneNumberOrUserName);
        loadAccidentHistoryListByUser(phoneNumberOrUserName);
    }

    public LiveData<List<AccidentHistory>> getAccidentHistoryList()
    {
        if (accidentHistoryList.getValue() == null)
        {
            accidentHistoryList.postValue(new ArrayList<>());
        }
        return accidentHistoryList;
    }

    public Drawable getUserImage()
    {
        return userImage;
    }

    public User getCurrUser()
    {
        return currUser;
    }

    public LiveData<Boolean> isUserInformationLoaded()
    {
        return userInformationLoaded;
    }

    public LiveData<Event<Boolean>> isAddAccidentHistorySuccess()
    {
        return addAccidentHistorySuccess;
    }

    private void loadUserInformation(String phoneNumber)
    {
        userRepository.getUserByPhoneNumber(phoneNumber, result ->
        {
            if (result instanceof Result.Success)
            {
                currUser = ((Result.Success<User>) result).getData();

                userInformationLoaded.setValue(true);
            }
        });
    }

    private void loadNoPhoneNumberUserInformation(String userName)
    {
        userRepository.noPhoneNumberGetUser(userName, result ->
        {
            if (result instanceof Result.Success)
            {
                currUser = ((Result.Success<User>) result).getData();
                userInformationLoaded.setValue(true);
            }
        });
    }

    private void loadUserImage(String phoneNumber)
    {
        userRepository.loadUserImageDrawable(phoneNumber, new CompletedCallback<Result<Drawable>>()
        {
            @Override
            public void onComplete(Result<Drawable> drawableResult)
            {
                if (drawableResult instanceof Result.Success)
                {
                    userImage = ((Result.Success<Drawable>) drawableResult).getData();
                }
                else
                {

                }
            }
        });
    }

    private void loadAccidentHistoryListByUser(String phoneNumber)
    {
        accidentRepository.registerAccidentHistoryListListener(phoneNumber, new ListenerCallback<List<AccidentHistory>>()
        {
            @Override
            public void onUpdate(List<AccidentHistory> result)
            {
                accidentHistoryList.postValue(result);
            }
        });
    }

}
