package com.example.contacts;

import com.example.contacts.ui.MainActivity;
import dagger.Component;

import javax.inject.Singleton;

@Singleton
@Component(modules = {DataModule.class})
public interface ApplicationComponent {
    void inject(MainActivity baseActivity);
}
