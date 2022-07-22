package com.gausslab.managerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.gausslab.managerapp.dataSource.FirebaseDataSource;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseDataSource ds = new FirebaseDataSource();
        UserRepository.getInstance().setDataSource(ds);
        setContentView(R.layout.activity_main);
    }
}