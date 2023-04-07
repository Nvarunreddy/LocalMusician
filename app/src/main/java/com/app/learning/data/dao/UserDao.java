package com.app.learning.data.dao;

import androidx.room.Dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.app.learning.data.entities.UserData;

@Dao
public interface UserDao {

    @Insert
    void insertUser(UserData user);

    @Query("SELECT * FROM UserData WHERE email = :email AND password = :password")
    UserData getUserByEmailAndPassword(String email, String password);

    @Query("SELECT * FROM UserData WHERE user_data_id=:instructorId")
    UserData getUserDataById(int instructorId);

    @Query("SELECT * FROM UserData WHERE email = :email")
    UserData getUserDataByEmail(String email);
}

