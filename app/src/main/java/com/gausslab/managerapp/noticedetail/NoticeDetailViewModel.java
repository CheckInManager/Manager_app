package com.gausslab.managerapp.noticedetail;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

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
    private final MutableLiveData<Boolean> deleteSuccess = new MutableLiveData<>(false);

    private List<Worksite> worksiteList = new ArrayList<>();
    private Notice currNotice;

    public void loadNoticeDetail(String keyValue) {
        worksiteRepository.getNoticeDetailByName(keyValue, result -> {
            if (result instanceof Result.Success) {
                currNotice = ((Result.Success<Notice>)result).getData();
                noticeDetailLoaded.postValue(true);
            }else{
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

    public void deleteExNotice(Notice exNotice){
        worksiteRepository.deleteNotice(exNotice.getKeyValue(), result -> {
            if(result instanceof Result.Success){
                deleteSuccess.postValue(true);
            }else{
                deleteSuccess.postValue(false);
            }
        });
    }

    public void setDeletedSuccess(){
        deleteSuccess.postValue(false);
    }

    public void changeNotice(Notice notice){
        worksiteRepository.changeNotice(notice, result->{
           if(result instanceof Result.Success){

           }
        });
    }

    public List<Worksite> getOpenWorksite() {
        return worksiteList;
    }

    public Notice getNoticeDetail(){
        return currNotice;
    }

    public LiveData<Boolean> isNoticeDetailLoaded() {
        return noticeDetailLoaded;
    }

    public LiveData<Boolean> openWorksiteListLoaded() {
        return openWorksiteListLoaded;
    }

    public LiveData<Boolean> isDeleteSuccess(){return deleteSuccess;}
}
