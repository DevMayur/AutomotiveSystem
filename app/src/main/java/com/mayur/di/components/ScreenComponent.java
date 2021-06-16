package com.mayur.di.components;

import com.mayur.di.scopes.ScreenScope;
import com.mayur.ui.activities.DetectorActivity;

import dagger.Component;

@ScreenScope
@Component(dependencies = ApplicationComponent.class)
public interface ScreenComponent {
    void inject(DetectorActivity detectorActivity);
}
