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
        @Query("SELECT * FROM result FOR SYSTEM_TIME BETWEEN '2021-01-01' AND '2021-12-31'"+
                "ORDER BY DESC")
        Result showResultOfTheWeek();

 */

        @Insert
        void insertResult(Result results);


        @Delete
        void deleteResult(Result result);
    }


