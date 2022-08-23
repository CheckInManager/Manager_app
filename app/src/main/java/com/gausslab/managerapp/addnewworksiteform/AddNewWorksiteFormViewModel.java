package com.gausslab.managerapp.addnewworksiteform;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.gausslab.managerapp.repository.WorksiteRepository;
import com.gausslab.managerapp.model.Result;
import com.gausslab.managerapp.model.Worksite;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class AddNewWorksiteFormViewModel extends ViewModel {
    private WorksiteRepository worksiteRepository = WorksiteRepository.getInstance();
    private MutableLiveData<Boolean> addWorksiteFormSuccess = new MutableLiveData<>(false);
    private MutableLiveData<Boolean> addWorksiteSuccess = new MutableLiveData<>(false);
    private final MutableLiveData<AddNewWorksiteFormState> addNewWorksiteFormState = new MutableLiveData<>(new AddNewWorksiteFormState(null, null, null, null, false));

    private String worksiteName = "";
    private String startDate = "";
    private String endDate = "";
    private String location = "";

    private List<Worksite> allWorksite = new ArrayList<>();
    private List<String> allWorksiteName = new ArrayList<>();

    public void addWorksite(Worksite worksite) {
        worksiteRepository.addWorksite(worksite, result ->
        {
            if (result instanceof Result.Success) {
                addWorksiteFormSuccess.postValue(true);
            } else {
                addWorksiteFormSuccess.postValue(false);
            }
        });
    }

    public void createQrForWorksite(Worksite toCreate) {
        worksiteRepository.createQrForWorksite(toCreate, result ->
        {
            if (result instanceof Result.Success) {
                addWorksiteSuccess.postValue(true);
            } else {
                addWorksiteSuccess.postValue(false);
            }
        });
    }

    public void onWorksiteNameChanged(String writeWorksiteName) {
        worksiteName = writeWorksiteName;
        if (writeWorksiteName.length() == 0) {
            addNewWorksiteFormState.setValue(new AddNewWorksiteFormState(null, null, null, null, false));
        } else if (!isWorksiteNameValid(worksiteName)) {
            addNewWorksiteFormState.setValue(new AddNewWorksiteFormState("worksite format is wrong", addNewWorksiteFormState.getValue().getStartDateErrorMessage(), addNewWorksiteFormState.getValue().getStartDateErrorMessage(), addNewWorksiteFormState.getValue().getLocationErrorMessage(), false));
        } else if (isStartDateValid(startDate) && isEndDateValid(endDate) && isLocationValid(location)) {
            addNewWorksiteFormState.setValue(new AddNewWorksiteFormState(null, null, null, null, true));
        } else if (addNewWorksiteFormState.getValue().getWorksiteNameErrorMessage() != null) {
            addNewWorksiteFormState.setValue(new AddNewWorksiteFormState(null, addNewWorksiteFormState.getValue().getStartDateErrorMessage(), addNewWorksiteFormState.getValue().getStartDateErrorMessage(), addNewWorksiteFormState.getValue().getLocationErrorMessage(), false));
        }
    }

    public void onStartDateChanged(String writeStartDate) {
        startDate = writeStartDate;
        if (writeStartDate.length() == 0) {
            addNewWorksiteFormState.setValue(new AddNewWorksiteFormState(null, null, null, null, false));
        } else if (!isStartDateValid(startDate)) {
            addNewWorksiteFormState.setValue(new AddNewWorksiteFormState(addNewWorksiteFormState.getValue().getWorksiteNameErrorMessage(), "StartDate format is wrong", addNewWorksiteFormState.getValue().getStartDateErrorMessage(), addNewWorksiteFormState.getValue().getLocationErrorMessage(), false));
        } else if (isWorksiteNameValid(worksiteName) && isEndDateValid(endDate) && isLocationValid(location)) {
            addNewWorksiteFormState.setValue(new AddNewWorksiteFormState(null, null, null, null, true));
        } else if (addNewWorksiteFormState.getValue().getStartDateErrorMessage() != null) {
            addNewWorksiteFormState.setValue(new AddNewWorksiteFormState(addNewWorksiteFormState.getValue().getWorksiteNameErrorMessage(), null, addNewWorksiteFormState.getValue().getStartDateErrorMessage(), addNewWorksiteFormState.getValue().getLocationErrorMessage(), false));
        }
    }

    public void onEndDateChanged(String writeEndDate) {
        endDate = writeEndDate;
        if (writeEndDate.length() == 0) {
            addNewWorksiteFormState.setValue(new AddNewWorksiteFormState(null, null, null, null, false));
        } else if (!isEndDateValid(endDate)) {
            addNewWorksiteFormState.setValue(new AddNewWorksiteFormState(addNewWorksiteFormState.getValue().getWorksiteNameErrorMessage(), addNewWorksiteFormState.getValue().getStartDateErrorMessage(), "LastDate format is wrong", addNewWorksiteFormState.getValue().getLocationErrorMessage(), false));
        } else if (isWorksiteNameValid(worksiteName) && isStartDateValid(startDate) && isLocationValid(location)) {
            addNewWorksiteFormState.setValue(new AddNewWorksiteFormState(null, null, null, null, true));
        } else if (addNewWorksiteFormState.getValue().getWorksiteNameErrorMessage() != null) {
            addNewWorksiteFormState.setValue(new AddNewWorksiteFormState(addNewWorksiteFormState.getValue().getWorksiteNameErrorMessage(), addNewWorksiteFormState.getValue().getStartDateErrorMessage(), null, addNewWorksiteFormState.getValue().getLocationErrorMessage(), false));
        }
    }

    public void onLocationChanged(String writeLocation) {
        location = writeLocation;
        if (writeLocation.length() == 0) {
            addNewWorksiteFormState.setValue(new AddNewWorksiteFormState(null, null, null, null, false));
        } else if (!isLocationValid(location)) {
            addNewWorksiteFormState.setValue(new AddNewWorksiteFormState(addNewWorksiteFormState.getValue().getWorksiteNameErrorMessage(), addNewWorksiteFormState.getValue().getStartDateErrorMessage(), addNewWorksiteFormState.getValue().getStartDateErrorMessage(), "Location is too short", false));
        } else if (isStartDateValid(startDate) && isStartDateValid(startDate) && isEndDateValid(endDate)) {
            addNewWorksiteFormState.setValue(new AddNewWorksiteFormState(null, null, null, null, true));
        } else if (addNewWorksiteFormState.getValue().getWorksiteNameErrorMessage() != null) {
            addNewWorksiteFormState.setValue(new AddNewWorksiteFormState(addNewWorksiteFormState.getValue().getWorksiteNameErrorMessage(), addNewWorksiteFormState.getValue().getStartDateErrorMessage(), addNewWorksiteFormState.getValue().getStartDateErrorMessage(), null, false));
        }
    }

    public boolean isWorksiteNameValid(String worksiteName) {
        return !(worksiteName.length() < 1);
    }

    public boolean isStartDateValid(String startDate) {
        return !(startDate.length() < 8);
    }

    public boolean isEndDateValid(String endDate) {
        return !(endDate.length() < 8);
    }

    public boolean isLocationValid(String location) {
        return !(location.length() < 1);
    }

    public boolean isDatesValid(String startDate, String lastDate) {
        String[] splitStartDate = startDate.split("/");
        String[] splitEndDate = lastDate.split("/");
        String strStartDate = String.join("", splitStartDate);
        String strEndDate = String.join("", splitEndDate);
        if (Integer.parseInt(strStartDate) > Integer.parseInt(strEndDate)) {
            return false;
        } else {
            return true;
        }
    }

    public boolean checkDate(String date){
        try{
            SimpleDateFormat dateFormatParser = new SimpleDateFormat("yyyy/MM/dd");
            dateFormatParser.setLenient(false);
            dateFormatParser.parse(date);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public void loadAllWorksite(){
        worksiteRepository.getAllWorksite(result->{
            allWorksite =((Result.Success<List<Worksite>>)result).getData();
            allWorksiteName = new ArrayList<>();
            for(int i=0;i<allWorksite.size();i++){
                allWorksiteName.add(allWorksite.get(i).getWorksiteName());
            }
        });
    }
    public boolean checkSameWorksiteName(String newWorksiteName){
        if(!allWorksiteName.contains(newWorksiteName)){
            return true;
        }else{
            return false;
        }
    }

    public LiveData<Boolean> addWorksiteFormSuccess() {
        return addWorksiteFormSuccess;
    }

    public LiveData<Boolean> addWorksiteSuccess() {
        return addWorksiteSuccess;
    }

    public LiveData<AddNewWorksiteFormState> getAddNewWorksiteFormState() {
        return addNewWorksiteFormState;
    }
}
