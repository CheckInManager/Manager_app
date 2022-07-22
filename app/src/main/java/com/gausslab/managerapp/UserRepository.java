package com.gausslab.managerapp;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.gausslab.managerapp.dataSource.DataSource;
import com.gausslab.managerapp.model.Result;
import com.gausslab.managerapp.model.Worksite;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.File;

public class UserRepository {
    private static volatile UserRepository INSTANCE = new UserRepository();
    private DataSource dataSource;
    private FileService fileService;

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

            }
        });
    }

    private void generateWorksiteQr(Worksite worksite, UserRepositoryCallback<Result> callback) {
        String toEncode = "gausslab.managerapp.worksite_" + worksite.getWorkName();

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
        }catch(WriterException e){
                e.printStackTrace();
            }
        }

        public void setDataSource (DataSource ds){
            this.dataSource = ds;
        }

        public interface UserRepositoryCallback<Result> {
            void onComplete(Result result);
        }
    }
