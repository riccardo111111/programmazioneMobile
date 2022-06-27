package com.example.dazero.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.sql.Blob;

@Entity
public class Result {
    @PrimaryKey
    public int idResult;

    @ColumnInfo(name="result")
    public int idUser;
    public String photo;
    public String labels;
    public String date;
    public byte[] bytes;


}
