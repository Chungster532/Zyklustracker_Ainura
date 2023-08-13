package com.example.tracker_ainura;

import static java.time.LocalDate.parse;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.example.tracker_ainura.Database.RoomDB;
import com.example.tracker_ainura.Database.RoomDB_Impl;
import com.example.tracker_ainura.Models.Zyklen;
import com.example.tracker_ainura.databinding.ActivityNeuePeriodeBinding;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class NeuePeriodeActivity extends AppCompatActivity {

    private ActivityNeuePeriodeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNeuePeriodeBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.btnSpeichern.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                neuePeriodeVorbereiten();
                zusammenfassungZeigen();
                finish();
            }
        });
    }

    private void zusammenfassungZeigen() {
    }

    private View.OnClickListener neuePeriodeVorbereiten() {
        SharedPreferences prefs = getApplicationContext().getSharedPreferences("SharedPrefs", Context.MODE_PRIVATE);
        int tag = binding.datepickerNeuePeriode.getDayOfMonth();
        String monatStrFertig;
        String tagStrFertig;
        String jahr = Integer.toString(binding.datepickerNeuePeriode.getYear());
        int monat = binding.datepickerNeuePeriode.getMonth();
        monat += 1;
        if(tag<10){
            String tagStr = Integer.toString(tag);
            tagStrFertig = "0"+tagStr;
        }else{
            tagStrFertig = Integer.toString(tag);
        }
        if(monat<10){
            String monatStr = Integer.toString(monat);
            monatStrFertig = "0"+monatStr;
        }else{
            monatStrFertig = Integer.toString(monat);
        }
        String aktuellePeriode = tagStrFertig+"-"+monatStrFertig+"-"+jahr;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String letztePeriode = prefs.getString("letztePeriode", "");
        LocalDate date1 = parse(letztePeriode, formatter);
        LocalDate date2 = parse(aktuellePeriode, formatter);
        LocalDate periodePrefs = date2;
        DateTimeFormatter df = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String periodePrefsFertig = df.format(periodePrefs);
        Zyklen zyklus = new Zyklen();
        zyklus.setLaenge(zyklusLaengeAusrechnen(date2, prefs));
        neuePeriodeInPrefsSpeichern(periodePrefsFertig, prefs);
        date2 = date2.minusDays(1);
        zyklus.setStart(df.format(date1));
        zyklus.setEnde(df.format(date2));
        RoomDB database_zyklen = RoomDB.getInstance(this);
        database_zyklen.zyklenDao().insert(zyklus);

        Intent returnIntent = new Intent(NeuePeriodeActivity.this, ZusammenfassungActivity.class);
        returnIntent.putExtra("zyklus", zyklus);
        startActivity(returnIntent);

        return null;
    }

    private String zyklusLaengeAusrechnen(LocalDate date2, SharedPreferences prefs) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String letztePeriode = prefs.getString("letztePeriode", "");
        int laenge = Integer.parseInt(prefs.getString("laenge", ""));
        LocalDate date1 = parse(letztePeriode, formatter);
        long differenz = ChronoUnit.DAYS.between(date1, date2);
        String laengeDb = Long.toString(differenz);
        int differenzKonvertiert = Long.valueOf(differenz).intValue();
        int durchschnitt = (differenzKonvertiert+laenge)/2;
        String fertigeLaenge = String.valueOf(durchschnitt);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("laenge", fertigeLaenge);
        editor.commit();
        return laengeDb;
    }

    public void neuePeriodeInPrefsSpeichern(String letztePeriode, SharedPreferences prefs) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("letztePeriode", letztePeriode);
        editor.commit();
    }
}