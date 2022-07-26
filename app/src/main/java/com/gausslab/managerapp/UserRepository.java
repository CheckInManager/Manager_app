package com.gausslab.managerapp;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.gausslab.managerapp.dataSource.DataSource;
import com.gausslab.managerapp.dataSource.DataSourceListenerCallback;
import com.gausslab.managerapp.model.Result;
import com.gausslab.managerapp.model.Worksite;
import com.google.firebase.firestore.DocumentSnapshot;
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

public class UserRepository {
    private static volatile UserRepository INSTANCE = new UserRepository();
    private DataSource dataSource;
    private FileService fileService;
    protected Executor executor;
    private final MutableLiveData<Boolean> worksiteListUpdated = new MutableLiveData<>(false);

    private Map<String, Drawable> worksiteQrDrawableMap = new HashMap<String, Drawable>();
    private final Map<String, MutableLiveData<Boolean>> qrLoadedMap = new HashMap<>();
    private Map<String, Worksite> worksiteMap = new HashMap<>();

    public static UserRepository getInstance() {
        return INSTANCE;
    }


    public void getTodayWorksite(final UserRepositoryCallback callback) {
        dataSource.getTodayWorksite(callback::onComplete);
    }

    public void addWorksite(final Worksite worksite, UserRepositoryCallback<Result> callback) {
        dataSource.addWorksite(worksite, callback::onComplete);
    }

    public void createQrForWorksite(final Worksite toAdd, UserRepositoryCallback<Result> callback) {
        generateWorksiteQr(toAdd, new UserRepositoryCallback<Result>() {
            @Override
            public void onComplete(Result result) {
                callback.onComplete(result);
            }
        });
    }

    private void generateWorksiteQr(Worksite worksite, UserRepositoryCallback<Result> callback) {
        String toEncode = "gausslab.managerapp.worksite_" + worksite.getWorkName();
        generateWorksiteQr_helper(toEncode, App.getWorksiteQrImagePath(worksite.getWorkName()), new UserRepositoryCallback<Result>() {
            @Override
            public void onComplete(Result result) {
                callback.onComplete(result);
            }
        });

    }

    private void generateWorksiteQr_helper(String toEncode, String localDestinationPath, UserRepositoryCallback<Result> callback) {
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
                        File localFile = ((Result.Success<File>) result).getData();
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

    public List<Worksite> getWorksiteList() {
        return new ArrayList<Worksite>(worksiteMap.values());
    }

    public Worksite getWorksite(String worksiteName) {
        if (worksiteMap.containsKey(worksiteName))
            return worksiteMap.get(worksiteName);
        return null;
    }

    public Drawable getQrDrawable(String workName) {
        return worksiteQrDrawableMap.get(workName);
    }

    public void loadQrDrawableForDevice(String workname, UserRepositoryCallback<Result> callback) {
        callback.onComplete(new Result.Loading("Loading QR drawable for : " + workname));
        fileService.getImageDrawable(App.getWorksiteQrImagePath(workname), new FileService.FileServiceCallback<Result<Drawable>>() {
            @Override
            public void onComplete(Result result) {
                if (result instanceof Result.Success) {
                    Drawable drawable = ((Result.Success<Drawable>) result).getData();
                    worksiteQrDrawableMap.put(workname, drawable);
                    qrLoadedMap.get(workname).postValue(true);
                }
                callback.onComplete(result);
            }
        });
    }

    public void loadWorksiteList(UserRepositoryCallback<Result> callback) {
        dataSource.getDocumentsFromCollection("worksite", new DataSourceListenerCallback<Result>() {
            @Override
            public void onUpdate(Result result) {
                if (result instanceof Result.Success) {
                    Map<String, Worksite> newMap = ((Result.Success<Map<String, Worksite>>) result).getData();
                    worksiteMap = newMap;
                }
                callback.onComplete(result);
            }
        });
    }


    public LiveData<Boolean> isQrLoaded(String workName) {
        if (!qrLoadedMap.containsKey(workName))
            qrLoadedMap.put(workName, new MutableLiveData<>(false));
        return qrLoadedMap.get(workName);
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


    public interface UserRepositoryCallback<Result> {
        void onComplete(Result result);
    }
}
