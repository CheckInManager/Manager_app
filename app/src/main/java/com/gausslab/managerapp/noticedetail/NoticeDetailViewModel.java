package com.gausslab.managerapp.noticedetail;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.gausslab.managerapp.model.Notice;
import com.gausslab.managerapp.model.Result;
import com.gausslab.managerapp.repository.WorksiteRepository;

public class NoticeDetailViewModel extends ViewModel {
    private final WorksiteRepository worksiteRepository = WorksiteRepository.getInstance();
    private final MutableLiveData<Boolean> noticeDetailLoaded = new MutableLiveData<>(false);

    private Notice currNotice;

    public void loadNoticeDetail(String noticeName, String worksiteName) {
        worksiteRepository.getNoticeDetailByName(noticeName, worksiteName, result -> {
            if (result instanceof Result.Success) {
                currNotice = ((Result.Success<Notice>)result).getData();
                noticeDetailLoaded.postValue(true);
            }else{
                noticeDetailLoaded.postValue(false);
            }
        });
    }

    public Notice getNoticeDetail(){
        return currNotice;
    }

    public LiveData<Boolean> isNoticeDetailLoaded() {
        return noticeDetailLoaded;
    }
}
