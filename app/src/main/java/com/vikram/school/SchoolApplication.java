package com.vikram.school;

import android.app.Application;

import com.vikram.school.utility.PreferenceManager;

public class SchoolApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        PreferenceManager.instance().init(this);
    }
}
