package com.example.dazero.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface UserDao {

    @Query("SELECT * FROM user")
    List<User> getAllUsers();

    @Query("SELECT * FROM user WHERE email LIKE :email AND " +
            "password LIKE :password LIMIT 1")
    User findProfile(String email, String password);

    @Query("SELECT * FROM user WHERE uid LIKE :id LIMIT 1")
    User findProfileById(int id);

    @Insert
    void insertUser(User... users);
/*
    @Query("UPDATE user SET name = :user.name, surname = :user.surname, password = :user.password" +
            ",mail = :user.mail WHERE uid = :user.id")

 */
    @Update
    void updateUser(User user);

    @Delete
    void delete(User user);
}
