package com.gausslab.managerapp.repository;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.gausslab.managerapp.App;
import com.gausslab.managerapp.FileService;
import com.gausslab.managerapp.datasource.CompletedCallback;
import com.gausslab.managerapp.datasource.DataSource;
import com.gausslab.managerapp.datasource.ListenerCallback;
import com.gausslab.managerapp.model.Result;
import com.gausslab.managerapp.model.User;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

public class UserRepository {

    private static volatile UserRepository INSTANCE = new UserRepository();
    protected Executor executor;
    private DataSource dataSource;
    private FileService fileService;
    private Map<String, List<User>> worksiteUsersMap = new HashMap<>();
    private Map<String, MutableLiveData<Boolean>> worksiteUsersLoaded = new HashMap<>();

    private Map<String, Drawable> userImageDrawableMap = new HashMap<String, Drawable>();

    public static UserRepository getInstance() {
        return INSTANCE;
    }

    public List<User> getUserListByWorksite(final String worksiteName) {
        return worksiteUsersMap.get(worksiteName);
    }

    public void registerWorksiteUserListListener(String worksiteName) {
        if (worksiteUsersMap.containsKey(worksiteName))
            return;

        worksiteUsersMap.put(worksiteName, new ArrayList<>());
        worksiteUsersLoaded.put(worksiteName, new MutableLiveData<>());
        dataSource.getUserListByWorksite(worksiteName, new ListenerCallback<Result<List<User>>>() {
            @Override
            public void onUpdate(Result<List<User>> result) {
                if (result instanceof Result.Success) {
                    worksiteUsersMap.put(worksiteName, ((Result.Success<List<User>>) result).getData());
                    worksiteUsersLoaded.get(worksiteName).postValue(true);
                } else {

                }
            }
        });
    }

    public void getUserByPhoneNumber(final String phoneNumber, final CompletedCallback<Result<User>> callback) {
        dataSource.getUserByPhoneNumber(phoneNumber, callback);
    }

    public void changeUserInformation(final User changeUserInformation, final CompletedCallback<Result<String>> callback) {
        dataSource.changeUserInformation(changeUserInformation, callback);
    }
    public void changeNoPhoneNumberUserInformation(final User changeUserInformation, final CompletedCallback<Result<String>> callback) {
        dataSource.changeNoPhoneNumberUserInformation(changeUserInformation, callback);
    }

    public void addUser(final User userToAdd, final CompletedCallback<Result<String>> callback) {
        dataSource.addUser(userToAdd, callback);
    }

    public void addGuestUser(final User userToAdd, final CompletedCallback<Result<String>> callback) {
        dataSource.addGuestUser(userToAdd, callback);
    }

    public void loadPhoneNumberList(final CompletedCallback<Result<List<String>>> callback) {
        dataSource.getPhoneNumberList(callback);
    }

    public void saveUserImage(final User user, final Bitmap userImage, CompletedCallback<Result>callback){
        String localDestinationPath = App.getUserImagePath(user.getPhoneNumber());
        fileService.saveBitmapToDisk(localDestinationPath, userImage, new FileService.FileServiceCallback<Result<File>>() {
            @Override
            public void onComplete(Result<File> result) {
                if(result instanceof Result.Success){
                    File localFile = ((Result.Success<File>)result).getData();
                    fileService.uploadFileToDatabase(localFile, localDestinationPath, new FileService.FileServiceCallback<Result<Uri>>() {
                        @Override
                        public void onComplete(Result<Uri> result) {
                            callback.onComplete(result);
                        }
                    });
                }else{
                    callback.onComplete(new Result.Error(new Exception("UserRepository : saveUserImage() : Problem saving image bitmap to disk")));
                }
            }
        });
    }

    public void saveNoPhoneNumberUserImage(final User user, final Bitmap userImage, CompletedCallback<Result>callback){
        String localDestinationPath = App.getUserImagePath(user.getUserName());
        fileService.saveBitmapToDisk(localDestinationPath, userImage, new FileService.FileServiceCallback<Result<File>>() {
            @Override
            public void onComplete(Result<File> result) {
                if(result instanceof Result.Success){
                    File localFile = ((Result.Success<File>)result).getData();
                    fileService.uploadFileToDatabase(localFile, localDestinationPath, new FileService.FileServiceCallback<Result<Uri>>() {
                        @Override
                        public void onComplete(Result<Uri> result) {
                            callback.onComplete(result);
                        }
                    });
                }else{
                    callback.onComplete(new Result.Error(new Exception("UserRepository : saveUserImage() : Problem saving image bitmap to disk")));
                }
            }
        });
    }

    public void loadUserImageDrawable(String phoneNumber, CompletedCallback<Result<Drawable>> callback){
        fileService.getImageDrawable(App.getUserImagePath(phoneNumber), new FileService.FileServiceCallback<Result<Drawable>>() {
            @Override
            public void onComplete(Result<Drawable> result) {
                if(result instanceof Result.Success){
                    Drawable drawable = ((Result.Success<Drawable>)result).getData();
                    userImageDrawableMap.put(phoneNumber,drawable);
                }
                callback.onComplete(result);
            }
        });
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

    public LiveData<Boolean> isUserListLoadedForWorksite(String worksite) {
        return worksiteUsersLoaded.get(worksite);
    }

}
