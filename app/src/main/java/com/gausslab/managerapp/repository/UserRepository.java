package com.gausslab.managerapp.repository;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;

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
    private Map<Long, List<User>> worksiteUsersMap = new HashMap<>(); // keyValue, userList
    private Map<Long, MutableLiveData<Boolean>> worksiteUsersLoaded = new HashMap<>();

    private Map<String, Drawable> userImageDrawableMap = new HashMap<String, Drawable>();

    public static UserRepository getInstance() {
        return INSTANCE;
    }

    public void registerWorksiteUserListListener(long keyValue, ListenerCallback<List<User>> callback) {
        worksiteUsersMap.put(keyValue, new ArrayList<>());
        worksiteUsersLoaded.put(keyValue, new MutableLiveData<>());
        dataSource.getUserListByWorksite(keyValue, new ListenerCallback<Result<List<User>>>() {
            @Override
            public void onUpdate(Result<List<User>> result) {
                if (result instanceof Result.Success) {
                    List<User> userList = ((Result.Success<List<User>>) result).getData();
                    worksiteUsersMap.put(keyValue, userList);
                    callback.onUpdate(userList);
                }
            }
        });
    }

    public void getUserByPhoneNumber(final String phoneNumber, final CompletedCallback<Result<User>> callback) {
        dataSource.getUserByPhoneNumber(phoneNumber, callback);
    }

    public void noPhoneNumberGetUser(final String userName, final CompletedCallback<Result<User>> callback) {
        dataSource.noPhoneNumberGetUser(userName, callback);
    }

    public void addOrUpdateUser(final User user, final CompletedCallback<Result<String>> callback) {
        dataSource.addOrUpdateUser(user, callback);
    }

    public void loadPhoneNumberList(final CompletedCallback<Result<List<String>>> callback) {
        dataSource.getPhoneNumberList(callback);
    }

    public void saveUserImage(final User user, final Bitmap userImage, CompletedCallback<Result> callback) {
        String localDestinationPath = App.getUserImagePath(user.getPhoneNumber());
        fileService.saveBitmapToDisk(localDestinationPath, userImage, new FileService.FileServiceCallback<Result<File>>() {
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
                    callback.onComplete(new Result.Error(new Exception("UserRepository : saveUserImage() : Problem saving image bitmap to disk")));
                }
            }
        });
    }

    public void saveNoPhoneNumberUserImage(final User user, final Bitmap userImage, CompletedCallback<Result> callback) {
        String localDestinationPath = App.getUserImagePath(user.getName());
        fileService.saveBitmapToDisk(localDestinationPath, userImage, new FileService.FileServiceCallback<Result<File>>() {
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
                    callback.onComplete(new Result.Error(new Exception("UserRepository : saveUserImage() : Problem saving image bitmap to disk")));
                }
            }
        });
    }

    public void loadUserImageDrawable(String phoneNumber, CompletedCallback<Result<Drawable>> callback) {
        fileService.getImageDrawable(App.getUserImagePath(phoneNumber), new FileService.FileServiceCallback<Result<Drawable>>() {
            @Override
            public void onComplete(Result<Drawable> result) {
                if (result instanceof Result.Success) {
                    Drawable drawable = ((Result.Success<Drawable>) result).getData();
                    userImageDrawableMap.put(phoneNumber, drawable);
                }
                callback.onComplete(result);
            }
        });
    }

    public void deleteUser(User Remove, CompletedCallback<Result<String>> callback) {
        dataSource.deleteUser(Remove, callback);
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
