package com.gausslab.managerapp.addaccidenthistoryform;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.gausslab.managerapp.model.AccidentHistory;
import com.gausslab.managerapp.model.Result;
import com.gausslab.managerapp.repository.AccidentRepository;

import java.util.Map;

public class AddAccidentHistoryFormViewModel extends ViewModel {
    private AccidentRepository accidentRepository = AccidentRepository.getInstance();
    private MutableLiveData<Boolean> addAccidentHistorySuccess = new MutableLiveData<>(false);
    private MutableLiveData<Boolean> accidentHistoryKeyLoaded = new MutableLiveData<>(false);

    private String accidentHistoryKey;

    public void addAccidentHistory(AccidentHistory accidentHistory) {
        accidentRepository.addAccidentHistory(accidentHistory, result -> {
            if (result instanceof Result.Success) {
                addAccidentHistorySuccess.postValue(true);
            } else {
                addAccidentHistorySuccess.postValue(false);
            }
        });
    }

    public void loadAccidentHistoryKey() {
        accidentRepository.getAccidentHistoryKey(result -> {
            if (result instanceof Result.Success) {
                accidentHistoryKey = ((Result.Success<String>) result).getData();
                accidentHistoryKeyLoaded.postValue(true);
            } else {
                accidentHistoryKeyLoaded.postValue(false);
            }
        });
    }

    public String getAccidentHistoryKey() {
        return accidentHistoryKey;
    }


    public LiveData<Boolean> isAddAccidentHistorySuccess() {
        return addAccidentHistorySuccess;
    }

    public LiveData<Boolean> isAccidentHistoryKeyLoaded() {
        return accidentHistoryKeyLoaded;
    }
}
