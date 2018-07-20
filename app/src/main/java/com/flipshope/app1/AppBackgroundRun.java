package com.flipshope.app1;


import android.app.Application;
import android.content.Intent;

public class AppBackgroundRun extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        startService(new Intent(this, BackgroundService.class));

    }
}