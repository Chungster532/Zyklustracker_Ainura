package com.example.tracker_ainura.Database;

import static androidx.room.OnConflictStrategy.REPLACE;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.tracker_ainura.Models.Notizen;

import java.util.List;

/**
 * Interface für Notizen-Entity
 * */
@Dao
public interface NotizenDao {

    @Insert(onConflict = REPLACE)
    void insert(Notizen notiz);

    @Query("select * from notizen order by id desc")
    List<Notizen> getAll();

    @Query("update notizen set datum = :datum, tagZyklus = :tagZyklus, training = :training, blutung = :blutung, stimmung = :stimmung, sonstiges = :sonstiges where id = :id")
    void update(int id, String datum, String tagZyklus, String training, String blutung, String stimmung, String sonstiges);

    @Delete
    void delete(Notizen notiz);
}
