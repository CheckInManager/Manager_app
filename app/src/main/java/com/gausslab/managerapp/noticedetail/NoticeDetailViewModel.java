package com.gausslab.managerapp.noticedetail;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.gausslab.managerapp.datasource.CompletedCallback;
import com.gausslab.managerapp.model.Notice;
import com.gausslab.managerapp.model.Result;
import com.gausslab.managerapp.model.Worksite;
import com.gausslab.managerapp.repository.WorksiteRepository;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class NoticeDetailViewModel extends ViewModel
{
    private final WorksiteRepository worksiteRepository = WorksiteRepository.getInstance();
    private final MutableLiveData<Boolean> dataLoaded = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> worksiteUpdated = new MutableLiveData<>(false);
    private List<Worksite> worksiteList = new ArrayList<>();
    private Notice currNotice = null;
    private long currNoticeId = 0;

    public void updateNotice(String noticeTitle, String noticeMemo, Worksite selectedWorksite)
    {
        if (!isNoticeChanged(currNotice, noticeTitle, noticeMemo, selectedWorksite))
        {
            worksiteUpdated.postValue(true);
            return;
        }

        currNotice.setTitle(noticeTitle);
        currNotice.setContent(noticeMemo);
        currNotice.setWorksite(selectedWorksite);
        worksiteRepository.changeNotice(currNotice, new CompletedCallback<Result<String>>()
        {
            @Override
            public void onComplete(Result<String> result)
            {
                if (result instanceof Result.Success)
                    worksiteUpdated.postValue(true);
            }
        });
    }

    private void loadData()
    {
        loadWorksiteList(getTodayCalendarDateString(), new CompletedCallback<Result<String>>()
        {
            @Override
            public void onComplete(Result<String> result)
            {
                if (result instanceof Result.Success)
                {
                    loadNotice(currNoticeId, new CompletedCallback<Result<String>>()
                    {
                        @Override
                        public void onComplete(Result<String> result)
                        {
                            if (result instanceof Result.Success)
                            {
                                dataLoaded.postValue(true);
                            }
                        }
                    });
                }
            }
        });
        //노티스 정보
        //worksite list
    }

    private void loadWorksiteList(String todayCalString, CompletedCallback<Result<String>> callback)
    {
        worksiteRepository.getTodayWorksite(todayCalString, new CompletedCallback<Result<List<Worksite>>>()
        {
            @Override
            public void onComplete(Result<List<Worksite>> result)
            {
                if (result instanceof Result.Success)
                {
                    worksiteList = ((Result.Success<List<Worksite>>) result).getData();
                    callback.onComplete(new Result.Success<String>("Success"));
                }
                else
                    callback.onComplete(new Result.Error(new Exception("AHHH")));
            }
        });
    }

    private void loadNotice(long noticeId, CompletedCallback<Result<String>> callback)
    {
        worksiteRepository.getNotice(noticeId, new CompletedCallback<Result<Notice>>()
        {
            @Override
            public void onComplete(Result<Notice> result)
            {
                if (result instanceof Result.Success)
                {
                    currNotice = ((Result.Success<Notice>) result).getData();
                    callback.onComplete(new Result.Success<String>("Success"));
                }
                else
                {
                    callback.onComplete(new Result.Error(((Result.Error) result).getError()));
                }
            }
        });
    }

    public List<Worksite> getWorksiteList()
    {
        return worksiteList;
    }

    public Notice getCurrNotice()
    {
        return currNotice;
    }

    public void setCurrNoticeId(long noticeId)
    {
        this.currNoticeId = noticeId;
        loadData();
    }

    public LiveData<Boolean> isDataLoaded()
    {
        return dataLoaded;
    }

    public LiveData<Boolean> isWorksiteUpdated()
    {
        return worksiteUpdated;
    }

    private String getTodayCalendarDateString()
    {
        Calendar cal = Calendar.getInstance();
        String todayCal = ((cal.get(Calendar.YEAR)) + "" + (cal.get(Calendar.MONTH) + 1) + "" + (cal.get(Calendar.DATE)));

        String todayMonthCal = ((cal.get(Calendar.MONTH) + 1) + "");
        String todayDayCal = ((cal.get(Calendar.DATE)) + "");

        if (todayMonthCal.length() < 2 && todayDayCal.length() < 2)
        {
            todayCal = ((cal.get(Calendar.YEAR)) + "0" + (cal.get(Calendar.MONTH) + 1) + "0" + (cal.get(Calendar.DATE)));
        }
        else if (todayMonthCal.length() < 2)
        {
            todayCal = ((cal.get(Calendar.YEAR)) + "0" + (cal.get(Calendar.MONTH) + 1) + "" + (cal.get(Calendar.DATE)));
        }
        else if (todayDayCal.length() < 2)
        {
            todayCal = ((cal.get(Calendar.YEAR)) + "" + (cal.get(Calendar.MONTH) + 1) + "0" + (cal.get(Calendar.DATE)));
        }
        return todayCal;
    }

    private boolean isNoticeChanged(Notice currNotice, String noticeName, String noticeMemo, Worksite worksite)
    {
        return !(currNotice.getTitle().equals(noticeName) &&
                 currNotice.getContent().equals(noticeMemo) &&
                 currNotice.getWorksite().equals(worksite));
    }
}
