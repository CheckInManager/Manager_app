package com.gausslab.managerapp.qremailfragment;

import android.graphics.drawable.Drawable;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.gausslab.managerapp.repository.WorksiteRepository;
import com.gausslab.managerapp.datasource.CompletedCallback;
import com.gausslab.managerapp.model.Result;
import com.gausslab.managerapp.model.Worksite;

public class QrEmailViewModel extends ViewModel {
    private WorksiteRepository worksiteRepository = WorksiteRepository.getInstance();
    private MutableLiveData<Boolean> isQrLoaded = new MutableLiveData<>(false);

    private Worksite currWorksite;
    private Drawable qrImage;

    private Worksite worksite;

    public void setWorksite(String keyValue) {
        loadWorksite(keyValue);
        if(worksite!=null){
            currWorksite = worksiteRepository.getWorksite(worksite.getWorksiteName());
        }
        worksiteRepository.loadQrDrawableForWorksite(keyValue, new CompletedCallback<Result<Drawable>>() {
            @Override
            public void onComplete(Result<Drawable> drawableResult) {
                if (drawableResult instanceof Result.Success) {
                    qrImage = ((Result.Success<Drawable>) drawableResult).getData();
                    isQrLoaded.postValue(true);
                } else {
                    //오류 보여주기
                }
            }
        });
    }

    private void loadWorksite(String key){
        worksiteRepository.getWorksiteByKey(key, new CompletedCallback<Result<Worksite>>() {
            @Override
            public void onComplete(Result<Worksite> result) {
                if(result instanceof Result.Success){
                    worksite = ((Result.Success<Worksite>)result).getData();
                }else{
                    //Error
                }
            }
        });
    }

    public LiveData<Boolean> isQrImageLoaded() {
        return isQrLoaded;
    }

    public Drawable getQrImage() {
        return qrImage;
    }


}
