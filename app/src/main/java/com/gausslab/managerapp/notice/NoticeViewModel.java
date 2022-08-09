package com.gausslab.managerapp.notice;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.gausslab.managerapp.model.Notice;
import com.gausslab.managerapp.model.Result;
import com.gausslab.managerapp.repository.WorksiteRepository;

import java.util.List;

public class NoticeViewModel extends ViewModel {
    private final WorksiteRepository worksiteRepository = WorksiteRepository.getInstance();

    public void loadNoticeList(){
        worksiteRepository.registerNoticeListListener();
    }

    public void deleteNotice(String noticeName, String worksiteName){
        worksiteRepository.deleteNotice(noticeName, worksiteName, result->{
           if(result instanceof Result.Success){

           } else{

           }
        });
    }

    public List<Notice> getNoticeList(){
        return worksiteRepository.getNoticeList();
    }

    public LiveData<Boolean> isNoticeListLoaded(){
        return worksiteRepository.isNoticeListLoaded();
    }

}
