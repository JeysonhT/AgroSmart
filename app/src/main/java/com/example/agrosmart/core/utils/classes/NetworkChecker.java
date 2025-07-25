package com.example.agrosmart.core.utils.classes;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
public class NetworkChecker {

    public static boolean isInternetAvailable(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnected();
    }
}
