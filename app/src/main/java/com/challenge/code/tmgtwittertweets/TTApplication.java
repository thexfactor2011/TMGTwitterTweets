package com.challenge.code.tmgtwittertweets;

import android.app.Application;

import com.twitter.sdk.android.core.Twitter;

/**
 * Created by Harry Anuszewski on 4/22/2019.
 */
public class TTApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Twitter.initialize(this);
    }
}
