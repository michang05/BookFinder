package com.dmi.test.bookfinder;

import android.app.Application;

/**
 * Created by Mikey on 7/2/2016.
 */
public class DmiTestApplication extends Application {


    private static DmiTestApplication sInstance;


    @Override
    public void onCreate() {
        super.onCreate();

        sInstance = this;
    }

    public static DmiTestApplication getInstance() {
        return sInstance;
    }
}
