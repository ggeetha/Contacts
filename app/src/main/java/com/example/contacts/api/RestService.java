package com.example.contacts.api;

import com.example.contacts.model.UserResponse;
import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RestService {

    @GET("users")
    Single<UserResponse> getUsersData(@Query("page") int page);
}
