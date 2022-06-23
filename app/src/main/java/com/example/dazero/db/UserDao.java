package com.example.dazero.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface UserDao {

    @Query("SELECT * FROM user")
    List<User> getAllUsers();

    @Query("SELECT * FROM user WHERE email LIKE :email AND " +
            "password LIKE :password LIMIT 1")
    User findProfile(String email, String password);

    @Insert
    void insertUser(User... users);

    @Delete
    void delete(User user);
}
