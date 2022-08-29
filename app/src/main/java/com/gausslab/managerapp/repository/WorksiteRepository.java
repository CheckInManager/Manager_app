package com.gausslab.managerapp.repository;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.gausslab.managerapp.App;
import com.gausslab.managerapp.FileService;
import com.gausslab.managerapp.datasource.CompletedCallback;
import com.gausslab.managerapp.datasource.DataSource;
import com.gausslab.managerapp.datasource.ListenerCallback;
import com.gausslab.managerapp.model.Notice;
import com.gausslab.managerapp.model.Result;
import com.gausslab.managerapp.model.Worksite;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

public class WorksiteRepository {
    private static volatile WorksiteRepository INSTANCE = new WorksiteRepository();
    private DataSource dataSource;
    private FileService fileService;
    protected Executor executor;

    private Map<Long, Drawable> worksiteQrDrawableMap = new HashMap<Long, Drawable>();
    private MutableLiveData<Boolean> noticeListLoaded = new MutableLiveData<>(false);
    public List<Notice> noticeList = new ArrayList<>();
    public File localFile;

    public static WorksiteRepository getInstance() {
        return INSTANCE;
    }

    public void getTodayWorksite(final String todayCal, final CompletedCallback<Result<List<Worksite>>> callback) {
        dataSource.getTodayWorksiteList(todayCal, callback);
    }

    public void addWorksite(final Worksite worksite, CompletedCallback<Result<String>> callback) {
        dataSource.addWorksite(worksite, callback);
    }

    public void createQrForWorksite(final Worksite toAdd, CompletedCallback<Result<Uri>> callback) {
        generateWorksiteQr(toAdd, new CompletedCallback<Result<Uri>>() {
            @Override
            public void onComplete(Result result) {
                callback.onComplete(result);
            }
        });
    }

    private void generateWorksiteQr(Worksite worksite, CompletedCallback<Result<Uri>> callback) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                String toEncode = "gausslab.managerapp.worksite_" + worksite.getId();
                generateWorksiteQr_helper(toEncode, App.getWorksiteQrImagePath(worksite.getId()), new CompletedCallback<Result<Uri>>() {
                    @Override
                    public void onComplete(Result result) {
                        callback.onComplete(result);
                    }
                });
            }
        });
    }

    private void generateWorksiteQr_helper(String toEncode, String localDestinationPath, CompletedCallback<Result<Uri>> callback) {
        QRCodeWriter writer = new QRCodeWriter();
        try {
            BitMatrix bitMatrix = writer.encode(toEncode, BarcodeFormat.QR_CODE, 512, 512);
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }
            fileService.saveBitmapToDisk(localDestinationPath, bmp, new FileService.FileServiceCallback<Result<File>>() {
                @Override
                public void onComplete(Result<File> result) {
                    if (result instanceof Result.Success) {
                        localFile = ((Result.Success<File>) result).getData();
                        fileService.uploadFileToDatabase(localFile, localDestinationPath, new FileService.FileServiceCallback<Result<Uri>>() {
                            @Override
                            public void onComplete(Result<Uri> result) {
                                callback.onComplete(result);
                            }
                        });
                    } else {
                        callback.onComplete(new Result.Error(new Exception("DeviceRepository : createQr() : Problem saving QR bitmap to disk")));
                    }
                }
            });
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    public void getWorksiteByKey(long key, CompletedCallback<Result<Worksite>> callback) {
        dataSource.getWorksiteByKey(key, callback);
    }

    public Drawable getQrDrawable(String workName) {
        return worksiteQrDrawableMap.get(workName);
    }

    public File getQrFileForWorksite(Long key) {
        return fileService.getFile(App.getWorksiteQrImagePath(key));
    }

    public void loadQrDrawableForWorksite(long worksiteQrImagePath, CompletedCallback<Result<Drawable>> callback) {
        fileService.getImageDrawable(App.getWorksiteQrImagePath(worksiteQrImagePath), new FileService.FileServiceCallback<Result<Drawable>>() {
            @Override
            public void onComplete(Result result) {
                if (result instanceof Result.Success) {
                    Drawable drawable = ((Result.Success<Drawable>) result).getData();
                    worksiteQrDrawableMap.put(worksiteQrImagePath, drawable);
                }
                callback.onComplete(result);
            }
        });
    }



    public void registerNoticeListListener() {
        dataSource.getNoticeList(new ListenerCallback<Result<List<Notice>>>() {
            @Override
            public void onUpdate(Result<List<Notice>> result) {
                if (result instanceof Result.Success) {
                    noticeList = ((Result.Success<List<Notice>>) result).getData();
                    noticeListLoaded.postValue(true);
                } else {
                    noticeListLoaded.postValue(false);
                }
            }
        });
    }

    public void addNotice(final Notice notice, CompletedCallback<Result<String>> callback) {
        notice.setTimestamp(System.currentTimeMillis());
        dataSource.addNotice(notice, callback);
    }

    public void changeSpinnerStringToKeyValue(String worksiteName, CompletedCallback<Result<String>> callback) {
        dataSource.getWorksiteKeyFromString(worksiteName, callback);
    }

    public void deleteNotice(final Long noticeId, CompletedCallback<Result<String>> callback) {
        dataSource.deleteNotice(noticeId, callback);
    }

    public void changeNotice(final Notice notice, final CompletedCallback<Result<String>> callback) {

        dataSource.changeNotice(notice, callback);
    }

    public List<Notice> getNoticeList() {
        return noticeList;
    }

    public void getNotice(long noticeId, CompletedCallback<Result<Notice>> callback){
        dataSource.getNotice(noticeId, callback);
    }


    public void getAllWorksite(final CompletedCallback<Result<List<Worksite>>> callback) {
        dataSource.getAllWorksite(callback);
    }

    public void setExecutor(Executor exec) {
        this.executor = exec;
    }

    public void setDataSource(DataSource ds) {
        this.dataSource = ds;
    }

    public void setFileService(FileService fs) {
        this.fileService = fs;
    }

    public LiveData<Boolean> isNoticeListLoaded() {
        return noticeListLoaded;
    }

}
