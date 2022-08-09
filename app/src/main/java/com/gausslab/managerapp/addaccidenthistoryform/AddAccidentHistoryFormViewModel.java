package com.gausslab.managerapp.addaccidenthistoryform;

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

    public void addAccidentHistory(String userPhoneNumber, AccidentHistory accidentHistory){
        accidentRepository.addAccidentHistory(userPhoneNumber, accidentHistory, result->{
           if(result instanceof Result.Success){
               addAccidentHistorySuccess.postValue(true);
           } else{
               addAccidentHistorySuccess.postValue(false);
           }
        });
    }

    public LiveData<Boolean> isAddAccidentHistorySuccess(){return addAccidentHistorySuccess;}
}
