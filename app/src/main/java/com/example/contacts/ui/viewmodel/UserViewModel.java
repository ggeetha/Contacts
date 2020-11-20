package com.example.contacts.ui.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.contacts.model.USerDetailsPojo;
import com.example.contacts.repository.UserRepository;
import com.example.contacts.vo.Resource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import javax.inject.Inject;
import java.util.List;

public class UserViewModel extends ViewModel {

    UserRepository userRepository;
    private MutableLiveData<Resource<List<USerDetailsPojo>>> data = new MutableLiveData<>();

    @Inject
    public UserViewModel(UserRepository userRepository){

        this.userRepository = userRepository;

    }

    public MutableLiveData<Resource<List<USerDetailsPojo>>> getData() {
        return data;
    }

    public void setData(MutableLiveData<Resource<List<USerDetailsPojo>>> data) {
        this.data = data;
    }


    public Disposable fetchUser() {
        return userRepository.fetchUsers().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resource -> {
                    data.setValue(Resource.success(resource.data));
                }, throwable -> {
                    data.setValue(Resource.error(throwable.getLocalizedMessage(), null));

                });
    }
}
