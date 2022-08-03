package com.gausslab.managerapp;

import android.app.Service;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.provider.MediaStore;
import android.util.Log;

import com.gausslab.managerapp.datasource.CompletedCallback;
import com.gausslab.managerapp.datasource.FirebaseDataSource;
import com.gausslab.managerapp.model.Result;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
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

    public File createFile(String subPath, String fileName) {
        return createFile(subPath + fileName);
    }

    private File createFile(String destination) {
        File file = new File(imageStorageDir, destination);
        try {
            file.getParentFile().mkdirs();
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
                File localFile = createFile(destination);
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

    public void getImageDrawable(String filePath, FileServiceCallback<Result<Drawable>> callback) {
        executor.execute(() ->
        {
            File file = new File(imageStorageDir, filePath);
            if (file.exists()) {
                Drawable d = Drawable.createFromPath(file.getAbsolutePath());
                callback.onComplete(new Result.Success<Drawable>(d));
            } else {
                try {
                    file.getParentFile().mkdirs();
                    file.createNewFile();
                    firebaseDataSource.downloadFile(filePath, file, new CompletedCallback<Result<File>>() {
                        @Override
                        public void onComplete(Result result) {
                            if (result instanceof Result.Success) {
                                Log.d("DEBUG", "FileService : getImageDrawable() : " + filePath + " was downloaded");
                                File toReturn = ((Result.Success<File>) result).getData();
                                callback.onComplete(new Result.Success<Drawable>(Drawable.createFromPath(toReturn.getAbsolutePath())));
                            } else {
                                Log.d("DEBUG", "FileService : getImageDrawable() : " + filePath + " failed to download");
                                Log.d("DEBUG", ((Result.Error) result).getError().getMessage());
                                file.delete();
                                callback.onComplete(result);
                            }
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    public void uploadFileToDatabase(File toSave, String destination, FileServiceCallback<Result<Uri>> callback) {
        firebaseDataSource.uploadFile(toSave, destination, new CompletedCallback<Result<Uri>>() {
            @Override
            public void onComplete(Result result) {
                callback.onComplete(result);
            }
        });
    }

    public void saveBitmapToMediaStore(String displayName, Bitmap toSave, FileServiceCallback<Result<String>> callback) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DISPLAY_NAME, displayName);
                values.put(MediaStore.Images.Media.TITLE, displayName + " image");
                values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
                try {
                    ContentResolver cr = getContentResolver();
                    Uri uri = cr.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                    OutputStream outputStream = cr.openOutputStream(uri);
                    toSave.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                    outputStream.flush();
                    outputStream.close();
                    callback.onComplete(new Result.Success<String>("Successfully Added Image"));
                } catch (Exception e) {
                    e.printStackTrace();
                    callback.onComplete(new Result.Error(e));
                }
            }
        });
    }

    public void setFirebaseDataSource(FirebaseDataSource fds) {
        this.firebaseDataSource = fds;
    }

    public void setExecutor(Executor e) {
        executor = e;
    }




    public interface FileServiceCallback<T> {
        void onComplete(T result);
    }

    public class LocalBinder extends Binder {
        FileService getService() {
            return FileService.this;
        }
    }

}
