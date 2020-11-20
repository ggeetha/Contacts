package com.example.contacts.repository;

import com.example.contacts.api.RestService;
import com.example.contacts.localdb.AppDatabase;
import com.example.contacts.model.USerDetailsPojo;
import com.example.contacts.networkresource.NetworkBoundResource;
import com.example.contacts.utils.ConfigConstants;
import com.example.contacts.vo.Resource;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.functions.Function;

import javax.inject.Inject;
import java.util.List;

public class UserRepository {
    AppDatabase appDatabase;
    RestService restService;

    @Inject
    public UserRepository(AppDatabase appDatabase, RestService restService) {
        this.appDatabase = appDatabase;
        this.restService = restService;
    }

    public Flowable<Resource<List<USerDetailsPojo>>> fetchUsers() {
        return Flowable.create(emitter -> new NetworkBoundResource<List<USerDetailsPojo>, List<USerDetailsPojo>, List<USerDetailsPojo>>(emitter) {

            @Override
            public Single<List<USerDetailsPojo>> getRemote() {
                return restService.getUsersData(ConfigConstants.PAGE_NUMBER).flatMap(userResponse -> Single.just(userResponse.getData()));
            }

            @Override
            public Flowable<List<USerDetailsPojo>> getLocal() {
                return appDatabase.userDao().getUserDetails();
            }

            @Override
            public void saveCallResult(List<USerDetailsPojo> data) {
                appDatabase.userDao().insert(data);
                if(ConfigConstants.PAGE_NUMBER <2) {
                    ConfigConstants.PAGE_NUMBER=+1;
                }
            }

            @Override
            public Function<List<USerDetailsPojo>, List<USerDetailsPojo>> mapper() {
                return (responseMap) -> responseMap;
            }

            @Override
            public boolean shouldFetch(List<USerDetailsPojo> data) {
                return true;
            }
        }, BackpressureStrategy.BUFFER);
    }
}
