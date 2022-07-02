package com.example.dazero.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

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

    public Result(int idResult, int idUser, String photo, String labels, String date, byte[] bytes) {
        this.idResult = idResult;
        this.idUser = idUser;
        this.photo = photo;
        this.labels = labels;
        this.date = date;
        this.bytes = bytes;
    }
}
