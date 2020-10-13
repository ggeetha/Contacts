package com.example.contacts.localdb;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import com.example.contacts.model.USerDetailsPojo;
import com.example.contacts.dao.UserDao;

@Database(entities = USerDetailsPojo.class, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();
}
