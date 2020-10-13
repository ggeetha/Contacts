package com.example.contacts;

import android.app.Application;
import android.content.Context;
import androidx.multidex.MultiDex;

public class App extends Application {
    private static final String LOG_TAG = App.class.getName();
    private static App instance;
    private ApplicationComponent component;
    public static Context getContext() {
        return instance;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        inject();
        instance = this;
    }

    private void inject(){
        component = DaggerApplicationComponent.create();
    }

    public ApplicationComponent getApplicationComponent(){
        return component;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
