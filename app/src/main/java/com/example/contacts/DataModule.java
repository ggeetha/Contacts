package com.example.contacts;

import androidx.room.Room;

import com.example.contacts.api.RestService;
import com.example.contacts.localdb.AppDatabase;
import com.example.contacts.repository.UserRepository;
import com.example.contacts.ui.viewmodel.UserViewModel;
import com.example.contacts.ui.viewmodel.factory.UserViewModelFactory;
import com.example.contacts.utils.ConfigConstants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;
import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class DataModule {

    @Provides
    @Singleton
    AppDatabase provideAppDatabase() {
        return Room.databaseBuilder(App.getContext(),
                AppDatabase.class, "user_db").build();
    }

    @Provides
    @Singleton
    Gson provideGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        gsonBuilder.setLenient();
        return gsonBuilder.create();
    }

    @Provides
    @Singleton
    OkHttpClient provideOkhttpClient() {
        OkHttpClient.Builder client = new OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS);
        client.addInterceptor(chain -> {
            String contentType = chain.request().header("Content-Type");

            if (contentType == null) {
                contentType = "application/json";
            } else {
                //Log.i(LOG_TAG, "Content Type is" + contentType);
            }

            Request request = chain.request().newBuilder()
                    .header("Content-Type", contentType)
                    .build();
            return chain.proceed(request);
        });

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        client.addInterceptor(interceptor).build();

        return client.build();
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit(Gson gson, OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(ConfigConstants.API_URL)
                .client(okHttpClient)
                .build();
    }


    @Provides
    @Singleton
    RestService provideRestService(Retrofit retrofit) {
        return retrofit.create(RestService.class);
    }

    @Singleton
    @Provides
    public UserViewModelFactory provideUserViewModelFactory(UserRepository userRepository) {
        return new UserViewModelFactory(userRepository);
    }

    @Singleton
    @Provides
    public UserViewModel provideUserViewModel(UserRepository userRepository) {
        return new UserViewModel(userRepository);
    }

    @Singleton
    @Provides
    public UserRepository provideUserRepository(AppDatabase appDatabase, RestService restService) {
        return new UserRepository( appDatabase, restService);
    }

}
