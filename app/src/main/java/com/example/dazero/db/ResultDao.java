package com.example.dazero.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;
@Dao
public interface ResultDao {

        @Query("SELECT * FROM result")
        List<Result> getAllResults();


        @Query("SELECT * FROM result WHERE idResult LIKE :id LIMIT 1")
        Result findResultById(int id);

/*
        @Query("SELECT * FROM result WHERE date ('2022-07-02', INTERVAL 5 DAY) AS Result")
        Result showResultOfTheWeek();
 */

        @Insert
        void insertResult(Result results);


        @Delete
        void deleteResult(Result result);
    }


