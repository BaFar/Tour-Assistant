package com.example.dell.tourassistant;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by DELL on 10/26/2017.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
