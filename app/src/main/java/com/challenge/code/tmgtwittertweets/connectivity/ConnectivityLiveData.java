package com.challenge.code.tmgtwittertweets.connectivity;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


import androidx.lifecycle.LiveData;

/**
 * Created by Harry Anuszewski on 4/23/2019.
 */
//This LiveData emits NetWork information when network availability status changes and there is an active observer to it
public class ConnectivityLiveData extends LiveData<ConnectionModel> {
    private Context mContext;

    public ConnectivityLiveData(Context context) {
        mContext = context;
        postValue(new ConnectionModel(0, true));
    }

    @Override
    protected void onActive() {
        //onActive is called when there is an active observer to this LiveData
        //since active LiveData observers are there, add network callback listener to connectivity manager
        IntentFilter filter = new    IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        mContext.registerReceiver(networkReceiver, filter);
    }

    @Override
    protected void onInactive() {
        //onActive is called when there is no active observer to this LiveData
        //as no active observers exist, remove network callback from connectivity manager
        mContext.unregisterReceiver(networkReceiver);
    }

    private BroadcastReceiver networkReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getExtras()!=null) {
                ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

                boolean isConnected = activeNetwork != null &&
                        activeNetwork.isConnectedOrConnecting();

                if(isConnected) {
                    switch (activeNetwork.getType()){
                        case ConnectivityManager.TYPE_WIFI:
                            break;
                        case ConnectivityManager.TYPE_MOBILE:
                            break;
                    }
                    postValue(new ConnectionModel(1, true));
                } else {
                    postValue(new ConnectionModel(0 , false));
                }
            }
        }
    };
}