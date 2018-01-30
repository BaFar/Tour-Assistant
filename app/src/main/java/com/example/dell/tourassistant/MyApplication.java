package com.example.dell.tourassistant;

import android.app.Application;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

/**
 * Created by DELL on 10/26/2017.
 */

public class MyApplication extends Application {
    public static MyApplication mInstance;
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        mInstance = this;
    }

    public static synchronized MyApplication getInstance(){
        return  mInstance;
    }

    public void setConnectivityReceiverListener(ConnectivityReceiver.ConnectivityReceiverListener listener){
        ConnectivityReceiver.connectivityReceiverListener = listener;
        Log.d("receiver","receiver listener instance  initialized");
    }
}
