package com.example.contacts.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.example.contacts.model.USerDetailsPojo;
import io.reactivex.Flowable;

import java.util.List;

@Dao
public interface UserDao {

    @Query("SELECT * from user_table")
    Flowable<List<USerDetailsPojo>> getUserDetails();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<USerDetailsPojo> user);

    @Query("DELETE FROM user_table")
    void deleteAll();
}
