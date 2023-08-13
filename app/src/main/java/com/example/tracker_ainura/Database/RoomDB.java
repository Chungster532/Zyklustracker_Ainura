package com.example.tracker_ainura.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.tracker_ainura.Models.Notizen;
import com.example.tracker_ainura.Models.Zyklen;

@Database(entities = {Notizen.class, Zyklen.class}, version = 1, exportSchema = false)
public abstract class RoomDB extends RoomDatabase {
    private static RoomDB database;
    private static String DATABASE_NAME = "ZyklusTracker";

    public synchronized static RoomDB getInstance(Context context){
        if (database == null){
            database = Room.databaseBuilder(context.getApplicationContext(),
                    RoomDB.class, DATABASE_NAME)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return database;
    }

    public abstract NotizenDao notizenDao();
    public abstract ZyklenDao zyklenDao();
}
