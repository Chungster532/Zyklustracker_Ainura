package com.example.tracker_ainura.Models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "notizen")
public class Notizen implements Serializable {

    @PrimaryKey(autoGenerate = true)
    int id = 0;

    @ColumnInfo(name = "datum")
    String datum = "";

    @ColumnInfo(name = "training")
    String training = "";

    @ColumnInfo(name = "blutung")
    String blutung = "";

    @ColumnInfo(name = "stimmung")
    String stimmung = "";

    @ColumnInfo(name = "sonstiges")
    String sonstiges = "";

    @ColumnInfo(name = "tagZyklus")
    String tagZyklus = "";

    public String getTagZyklus() {
        return tagZyklus;
    }
    public void setTagZyklus(String tagZyklus) {
        this.tagZyklus = tagZyklus;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDatum() {
        return datum;
    }

    public void setDatum(String datum) {
        this.datum = datum;
    }

    public String getTraining() {
        return training;
    }

    public void setTraining(String training) {
        this.training = training;
    }

    public String getBlutung() {
        return blutung;
    }

    public void setBlutung(String blutung) {
        this.blutung = blutung;
    }

    public String getStimmung() {
        return stimmung;
    }

    public void setStimmung(String stimmung) {
        this.stimmung = stimmung;
    }

    public String getSonstiges() {
        return sonstiges;
    }

    public void setSonstiges(String sonstiges) {
        this.sonstiges = sonstiges;
    }
}
