package com.gausslab.managerapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;

import com.gausslab.managerapp.qremailfragment.IOnBackPressed;
import com.gausslab.managerapp.repository.AccidentRepository;
import com.gausslab.managerapp.repository.UserRepository;
import com.gausslab.managerapp.repository.WorksiteRepository;
import com.gausslab.managerapp.datasource.FirebaseDataSource;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    FileService fileService;
    WorksiteRepository worksiteRepository;
    UserRepository userRepository;
    AccidentRepository accidentRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ExecutorService executorService = Executors.newFixedThreadPool(2);
        FirebaseDataSource ds = new FirebaseDataSource();
        worksiteRepository = WorksiteRepository.getInstance();
        worksiteRepository.setDataSource(ds);
        worksiteRepository.setExecutor(executorService);
        userRepository = UserRepository.getInstance();
        userRepository.setDataSource(ds);
        userRepository.setExecutor(executorService);
        accidentRepository = AccidentRepository.getInstance();
        accidentRepository.setDataSource(ds);
        accidentRepository.setExecutor(executorService);

        ServiceConnection connection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder service) {
                FileService.LocalBinder binder = (FileService.LocalBinder) service;
                fileService = binder.getService();
                fileService.setFirebaseDataSource(ds);
                worksiteRepository.setFileService(fileService);
                userRepository.setFileService(fileService);
                accidentRepository.setFileService(fileService);
                fileService.setExecutor(executorService);
                App.setFileService(fileService);
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {

            }
        };
        Intent intent = new Intent(this, FileService.class);
        startService(intent);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);

        setContentView(R.layout.activity_main);
    }

//    @Override
//    public void onBackPressed(){
//        List<Fragment> fragmentList = getSupportFragmentManager().getFragments();
//        if(fragmentList !=null){
//            for(Fragment fragment: fragmentList){
//                if(fragment instanceof IOnBackPressed){
//                    ((IOnBackPressed)fragment).onBackPressed();
//                }
//            }
//        }
//    }
}