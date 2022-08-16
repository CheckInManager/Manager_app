package com.gausslab.managerapp.accidenthistorydetail;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.gausslab.managerapp.Event;
import com.gausslab.managerapp.model.AccidentHistory;
import com.gausslab.managerapp.model.Result;
import com.gausslab.managerapp.repository.AccidentRepository;

public class AccidentHistoryDetailViewModel extends ViewModel {
    private final AccidentRepository accidentRepository = AccidentRepository.getInstance();
    private final MutableLiveData<Event<Boolean>> accidentHistoryUpdateSuccessful = new MutableLiveData<>();

    public void changeAccidentHistory(AccidentHistory accidentHistory) {
        accidentRepository.changeAccidentHistory(accidentHistory, result -> {
            if (result instanceof Result.Success) {
                accidentHistoryUpdateSuccessful.postValue(new Event<Boolean>(true));
            }
        });
    }

//    public void deleteExAccidentHistory(AccidentHistory exAccidentHistory) {
//        accidentRepository.deleteAccidentHistory(exAccidentHistory.getKeyValue(), result -> {
//            if (result instanceof Result.Success) {
//                deletedSuccess.postValue(true);
//            } else {
//                deletedSuccess.postValue(false);
//            }
//        });
//    }
//
//    public void setDeletedSuccess(){
//        deletedSuccess.postValue(false);
//    }

    public LiveData<Event<Boolean>> isAccidentHistoryUpdateSuccessful() {
        return accidentHistoryUpdateSuccessful;
    }
}
