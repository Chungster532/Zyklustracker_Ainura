package com.example.tracker_ainura.Models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "zyklen")
public class Zyklen implements Serializable { //Serializable: enables data to be moced from one activity to another

    @PrimaryKey(autoGenerate = true)
    int id = 0;

    @ColumnInfo(name="start")
    String start = "";

    @ColumnInfo(name = "ende")
    String ende = "";

    @ColumnInfo(name = "laenge")
    String laenge = "";

    @ColumnInfo(name = "laengePeriode")
    String laengePeriode = "";

    public String getLaengePeriode() {return laengePeriode;}

    public void setLaengePeriode(String laengePeriode) {this.laengePeriode = laengePeriode;}

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getStart() {
        return start;
    }
    public void setStart(String start) {
        this.start = start;
    }

    public String getEnde() {
        return ende;
    }
    public void setEnde(String ende) {
        this.ende = ende;
    }

    public String getLaenge() {
        return laenge;
    }
    public void setLaenge(String laenge) {
        this.laenge = laenge;
    }
}
