package com.gausslab.managerapp.accidenthistorydetail;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.gausslab.managerapp.Event;
import com.gausslab.managerapp.datasource.CompletedCallback;
import com.gausslab.managerapp.model.AccidentHistory;
import com.gausslab.managerapp.model.Result;
import com.gausslab.managerapp.repository.AccidentRepository;

public class AccidentHistoryDetailViewModel extends ViewModel {
    private final AccidentRepository accidentRepository = AccidentRepository.getInstance();
    private final MutableLiveData<Event<Boolean>> accidentHistoryUpdateSuccessful = new MutableLiveData<>();
    private final MutableLiveData<Boolean> accidentHistoryLoaded = new MutableLiveData<>(false);
    private AccidentHistory accidentHistory;

    public void changeAccidentHistory(String description, String place, String date, String time) {
        if (accidentHistory.getDescription().equals(description) &&
                accidentHistory.getPlace().equals(place) &&
                accidentHistory.getDate().equals(date) &&
                accidentHistory.getTime().equals(time))
            return;

        accidentHistory.setDescription(description);
        accidentHistory.setPlace(place);
        accidentHistory.setDate(date);
        accidentHistory.setTime(time);

        accidentRepository.changeAccidentHistory(accidentHistory, result -> {
            if (result instanceof Result.Success) {
                accidentHistoryUpdateSuccessful.postValue(new Event<Boolean>(true));
            }
        });
    }

    public void loadAccidentHistory(String key) {
        accidentRepository.getAccidentHistory(key, new CompletedCallback<Result<AccidentHistory>>() {
            @Override
            public void onComplete(Result<AccidentHistory> result) {
                if (result instanceof Result.Success) {
                    accidentHistory = ((Result.Success<AccidentHistory>) result).getData();
                    accidentHistoryLoaded.postValue(true);
                } else {
                    //Error
                }
            }
        });
    }

    public AccidentHistory getAccidentHistory() {
        return accidentHistory;
    }

    public LiveData<Event<Boolean>> isAccidentHistoryUpdateSuccessful() {
        return accidentHistoryUpdateSuccessful;
    }

    public LiveData<Boolean> isAccidentHistoryLoaded() {
        return accidentHistoryLoaded;
    }
}
