package com.example.contacts.ui.viewmodel.factory;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.example.contacts.api.RestService;
import com.example.contacts.localdb.AppDatabase;
import com.example.contacts.repository.UserRepository;
import com.example.contacts.ui.viewmodel.UserViewModel;


import javax.inject.Inject;


public class UserViewModelFactory implements ViewModelProvider.Factory {

    private UserRepository userRepository;
    @Inject
    public UserViewModelFactory(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T)new UserViewModel(userRepository);
    }
}
