package com.gjrs.greedygame;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;


public final class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        MyApp vInstance = this;
        if (LeakCanary.isInAnalyzerProcess(vInstance)) {
            return;
        }
        LeakCanary.install(vInstance);
    }
}
