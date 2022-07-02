package com.example.dazero.db;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
@Entity
public class User {

    public int uid;
    @PrimaryKey(autoGenerate = true)
    public int idPrimary;
    public String name;
    public String surname;
    public String email;
    public String password;
}
