package com.gausslab.managerapp;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;

import com.gausslab.managerapp.dataSource.FirebaseDataSource;
import com.gausslab.managerapp.model.Result;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.Provider;
import java.util.concurrent.Executor;

public class FileService extends Service {

    private final IBinder binder = new LocalBinder();
    private File imageStorageDir;
    private Executor executor;
    private FirebaseDataSource firebaseDataSource;

    public FileService() {

    }

    @Override
    public void onCreate() {
        super.onCreate();
        imageStorageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    private File createFile(File parent, String fileName) {
        File file = new File(parent, fileName);
        try {
            boolean b = file.getParentFile().mkdirs();
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    public void saveBitmapToDisk(String destination, Bitmap toSave, FileServiceCallback<Result<File>> callback) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                File localFile = createImageFile(destination);
                try {
                    FileOutputStream outStream = new FileOutputStream(localFile);
                    toSave.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
                    outStream.flush();
                    outStream.close();
                    callback.onComplete(new Result.Success<File>(localFile));
                } catch (IOException e) {
                    e.printStackTrace();
                    callback.onComplete(new Result.Error(e));
                }
            }
        });
    }

    public File createImageFile(String destination) {
        return createFile(imageStorageDir, destination);
    }
    public void uploadFileToDatabase(File toSave, String destination, FileServiceCallback<Result<Uri>> callback)
    {
        firebaseDataSource.uploadFile(toSave, destination, new FirebaseDataSource.DataSourceCallback<Result<Uri>>()
        {
            @Override
            public void onComplete(Result result)
            {
                callback.onComplete(result);
            }
        });
    }

    public interface FileServiceCallback<Result> {
        void onComplete(Result result);
    }

    public class LocalBinder extends Binder {
        FileService getService() {
            return FileService.this;
        }
    }

}
