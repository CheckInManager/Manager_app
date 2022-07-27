package com.gausslab.managerapp.qremailfragment;

import android.graphics.drawable.Drawable;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.gausslab.managerapp.Repository.WorksiteRepository;
import com.gausslab.managerapp.model.Result;
import com.gausslab.managerapp.model.Worksite;

public class QrEmailViewModel extends ViewModel {
    private WorksiteRepository worksiteRepository = WorksiteRepository.getInstance();
    private MutableLiveData<Boolean> qrLoaded = new MutableLiveData<>(false);

    private Worksite currWorksite;
    private Drawable qrImage;

    public void setWorksite(String worksiteName) {
        currWorksite = worksiteRepository.getWorksite(worksiteName);

        worksiteRepository.loadQrDrawableForWorksite(worksiteName, new WorksiteRepository.WorksiteRepositoryCallback<Result<Drawable>>() {
            @Override
            public void onComplete(Result<Drawable> drawableResult) {
                if (drawableResult instanceof Result.Success) {
                    qrImage = ((Result.Success<Drawable>) drawableResult).getData();
                    qrLoaded.postValue(true);
                } else {
                    //오류 보여주기
                }
            }
        });
    }

    public LiveData<Boolean> isQrImageLoaded() {
        return qrLoaded;
    }

    public Drawable getQrImage() {
        return qrImage;
    }

    public Worksite getCurrWorksite() {
        return currWorksite;
    }
}
