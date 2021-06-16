package com.mayur.application;

import android.app.Application;

import com.mayur.di.components.ApplicationComponent;
import com.mayur.di.components.DaggerApplicationComponent;
import com.mayur.di.modules.ApplicationModule;


public class CarAssistantApplication extends Application {

    private ApplicationComponent applicationComponent;


    @Override
    public void onCreate() {
        super.onCreate();
        iniAppComponent();
    }

    private void iniAppComponent() {
        applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
    }

    public ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }
}
