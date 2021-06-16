package com.mayur.di.components;

import android.content.Context;

import com.mayur.di.modules.ApplicationModule;
import com.mayur.managers.SharedPreferencesManager;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ApplicationModule.class})
public interface ApplicationComponent {

    Context context();
    SharedPreferencesManager sharedPreferences();

}
