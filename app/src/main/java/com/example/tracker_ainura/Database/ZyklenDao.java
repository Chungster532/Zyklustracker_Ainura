package com.example.tracker_ainura.Database;

import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.tracker_ainura.Models.Zyklen;

import java.util.List;

@Dao
public interface ZyklenDao {

    @Insert(onConflict = REPLACE)
    void insert(Zyklen zyklus);

    @Query("select * from zyklen order by id desc")
    List<Zyklen> getAll();
}
