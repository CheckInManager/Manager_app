package com.gausslab.managerapp;

import android.app.Application;

public class App extends Application {
    private static FileService fileService;

    private static final WorksiteRepository WORKSITE_REPOSITORY = WorksiteRepository.getInstance();

    public static String getWorksiteQrImagePath(String workName)
    {
        return "worksiteQrImages/worksite_" + workName + ".jpg";
    }
}
