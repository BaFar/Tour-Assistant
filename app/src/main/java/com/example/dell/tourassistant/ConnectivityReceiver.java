package com.example.dell.tourassistant;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by DELL on 1/2/2018.
 */

public class ConnectivityReceiver extends BroadcastReceiver {

    public static ConnectivityReceiverListener connectivityReceiverListener;

    @Override
    public void onReceive(Context context, Intent intent) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = cm.getActiveNetworkInfo();

        boolean isConnected = activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();

        connectivityReceiverListener.onNetworkConnectionChanged(isConnected);

    }
    public static boolean isConnected(){
        ConnectivityManager cm = (ConnectivityManager) MyApplication
                .getInstance()
                .getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = cm.getActiveNetworkInfo();

        return activeNetworkInfo !=null && activeNetworkInfo.isConnectedOrConnecting();

    }

    public interface ConnectivityReceiverListener {
        void onNetworkConnectionChanged(boolean isConnected);
    }
}
