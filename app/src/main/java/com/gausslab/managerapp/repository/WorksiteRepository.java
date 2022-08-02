package com.gausslab.managerapp.repository;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import com.gausslab.managerapp.App;
import com.gausslab.managerapp.FileService;
import com.gausslab.managerapp.datasource.CompletedCallback;
import com.gausslab.managerapp.datasource.DataSource;
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

    private Map<String, Drawable> worksiteQrDrawableMap = new HashMap<String, Drawable>();
    private Map<String, Worksite> worksiteMap = new HashMap<>();

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
                String toEncode = "gausslab.managerapp.worksite_" + worksite.getWorksiteName()+worksite.getLocation()+worksite.getStartDate();
                generateWorksiteQr_helper(toEncode, App.getWorksiteQrImagePath(worksite.getWorksiteName()+worksite.getLocation()+worksite.getStartDate()), new CompletedCallback<Result<Uri>>() {
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

    public void loadQrDrawableForWorksite(String workPath, CompletedCallback<Result<Drawable>> callback) {
        fileService.getImageDrawable(App.getWorksiteQrImagePath(workPath), new FileService.FileServiceCallback<Result<Drawable>>() {
            @Override
            public void onComplete(Result result) {
                if (result instanceof Result.Success) {
                    Drawable drawable = ((Result.Success<Drawable>) result).getData();
                    worksiteQrDrawableMap.put(workPath, drawable);
                }
                callback.onComplete(result);
            }
        });
    }

    public void loadWorksiteNameList(CompletedCallback<Result<List<String>>> callback){
        dataSource.loadWorksiteNameList(callback);
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

}
