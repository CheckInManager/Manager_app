package com.gausslab.managerapp;

import android.app.Application;

import com.gausslab.managerapp.repository.WorksiteRepository;

public class App extends Application {
    private static FileService fileService;

    private static final WorksiteRepository WORKSITE_REPOSITORY = WorksiteRepository.getInstance();

    public static String getWorksiteQrImagePath(String worksiteKeyValue) {
        return "worksiteQrImages/worksite_" + worksiteKeyValue + ".jpg";
    }

    public static String getUserImagePath(String userPhoneNumber) {
        return "userImages/user_" + userPhoneNumber + ".jpg";
    }

    public static String getFileProvider() {
        return "com.gausslab.managerapp.fileprovider";
    }

    public static FileService getFileService() {
        return fileService;
    }

    public static void setFileService(FileService fs) {
        fileService = fs;
    }
}
