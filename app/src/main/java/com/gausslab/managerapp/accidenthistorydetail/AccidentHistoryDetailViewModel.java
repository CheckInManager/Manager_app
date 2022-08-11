package com.gausslab.managerapp.accidenthistorydetail;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.gausslab.managerapp.model.AccidentHistory;
import com.gausslab.managerapp.model.Result;
import com.gausslab.managerapp.repository.AccidentRepository;

public class AccidentHistoryDetailViewModel extends ViewModel {
    private final AccidentRepository accidentRepository = AccidentRepository.getInstance();
    private final MutableLiveData<Boolean> deletedSuccess = new MutableLiveData<>(false);

    public void changeAccidentHistory(AccidentHistory accidentHistory){
        accidentRepository.changeAccidentHistory(accidentHistory, result->{
           if(result instanceof Result.Success){

           }
        });
    }

    public void deleteExAccidentHistory(AccidentHistory exAccidentHistory){
        accidentRepository.deleteAccidentHistory(exAccidentHistory.getDescription(), exAccidentHistory.getPlace(), exAccidentHistory.getDate(), exAccidentHistory.getTime(),result -> {
            if(result instanceof Result.Success){
                deletedSuccess.postValue(true);
            }else{
                deletedSuccess.postValue(false);
            }
        });
    }

    public LiveData<Boolean> isDeletedSuccess(){return deletedSuccess;}
}
