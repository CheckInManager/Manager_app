package com.gausslab.managerapp.noticedetail;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.gausslab.managerapp.Event;
import com.gausslab.managerapp.model.Notice;
import com.gausslab.managerapp.model.Result;
import com.gausslab.managerapp.model.Worksite;
import com.gausslab.managerapp.repository.WorksiteRepository;

import java.util.ArrayList;
import java.util.List;

public class NoticeDetailViewModel extends ViewModel {
    private final WorksiteRepository worksiteRepository = WorksiteRepository.getInstance();
    private final MutableLiveData<Boolean> noticeDetailLoaded = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> openWorksiteListLoaded = new MutableLiveData<>(false);
    private final MutableLiveData<Event<Boolean>> noticeUpdateSuccessful = new MutableLiveData<>();
    private final MutableLiveData<Boolean> changedSpinnerStringLoaded = new MutableLiveData<>(false);
    private String worksiteKeyValue;

    private List<Worksite> worksiteList = new ArrayList<>();
    private Notice currNotice;

    public void loadNoticeDetail(String keyValue) {
        if(currNotice !=null){
            if(currNotice.getKeyValue().equals(keyValue)){
                return;
            }
        }
        worksiteRepository.getNoticeDetailByName(keyValue, result -> {
            if (result instanceof Result.Success) {
                currNotice = ((Result.Success<Notice>) result).getData();
                noticeDetailLoaded.postValue(true);
            } else {
                noticeDetailLoaded.postValue(false);
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

    public void changeNotice(Notice notice) {
        worksiteRepository.changeNotice(notice, result -> {
            if (result instanceof Result.Success) {
                noticeUpdateSuccessful.postValue(new Event<Boolean>(true));
            }
        });
    }

    public void updateMemoText(String newMemoText){
        currNotice.setMemo(newMemoText);
    }

    public void updateNoticeNameText(String newNoticeNameText){
        currNotice.setNoticeName(newNoticeNameText);
    }

    public void changeSpinnerStringToKeyValue(String worksiteName){
        worksiteRepository.changeSpinnerStringToKeyValue(worksiteName, result -> {
            if(result instanceof Result.Success){
                worksiteKeyValue =((Result.Success<String>)result).getData();
                changedSpinnerStringLoaded.postValue(true);
            }else {
                changedSpinnerStringLoaded.postValue(false);
            }
        });
    }

    public void changeState(){
        changedSpinnerStringLoaded.postValue(false);
    }

    public String getWorksiteKeyValue() {
        return worksiteKeyValue;
    }

    public LiveData<Boolean> isChangedSpinnerString() {
        return changedSpinnerStringLoaded;
    }


    public List<Worksite> getOpenWorksite() {
        return worksiteList;
    }

    public Notice getNoticeDetail() {
        return currNotice;
    }

    public LiveData<Boolean> isNoticeDetailLoaded() {
        return noticeDetailLoaded;
    }

    public LiveData<Boolean> openWorksiteListLoaded() {
        return openWorksiteListLoaded;
    }

    public LiveData<Event<Boolean>> isNoticeUpdateSuccessful() {
        return noticeUpdateSuccessful;
    }
}
