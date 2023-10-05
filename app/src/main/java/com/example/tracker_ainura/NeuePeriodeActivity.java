package com.example.tracker_ainura;

import static java.time.LocalDate.parse;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

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

        LocalDate date2 = parse(aktuellePeriode, formatter);
        LocalDate heute = LocalDate.now();
        String letztePeriode = prefs.getString("letztePeriode", "");
        LocalDate date1 = parse(letztePeriode, formatter);
        long daysBetween = ChronoUnit.DAYS.between(date1, date2.plusDays(1));

        if (daysBetween<15){
            Toast.makeText(this, "Zyklus kann nicht kürzer als 15 Tage sein", Toast.LENGTH_SHORT).show();
        }else if(date2.isAfter(heute)){
            Toast.makeText(this, "Es darf kein zukünftiges Datum ausgewählt werden", Toast.LENGTH_SHORT).show();
        }else if(date2.isBefore(date1)){
            Toast.makeText(this, "Neue Periode darf nicht vor vorheriger liegen", Toast.LENGTH_SHORT).show();
        }
        else{
            LocalDate periodePrefs = date2;
            String periodePrefsFertig = formatter.format(periodePrefs);
            Zyklen zyklus = new Zyklen();
            zyklus.setLaenge(Long.toString(daysBetween));
            neuePeriodeInPrefsSpeichern(periodePrefsFertig, prefs);
            date2 = date2.minusDays(1);
            zyklus.setStart(formatter.format(date1));
            zyklus.setEnde(formatter.format(date2));
            String laengePeriode = prefs.getString("laengeMens", "");
            zyklus.setLaengePeriode(laengePeriode);
            RoomDB database_zyklen = RoomDB.getInstance(this);
            database_zyklen.zyklenDao().insert(zyklus);

            durchschnittAusrechnen(daysBetween, prefs);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("Periode", true);
            editor.commit();

            Intent returnIntent = new Intent(NeuePeriodeActivity.this, ZusammenfassungActivity.class);
            returnIntent.putExtra("zyklus", zyklus);
            startActivity(returnIntent);
        }

        return null;
    }

    private void durchschnittAusrechnen(long daysBetween, SharedPreferences prefs) {
        SharedPreferences.Editor editor = prefs.edit();
        int laengeSum = prefs.getInt("laengeSum", 0);
        int zyklenAnz = prefs.getInt("zyklenInt", 0);

        int differenzKonvertiert = Long.valueOf(daysBetween).intValue();
        laengeSum = laengeSum + differenzKonvertiert;
        zyklenAnz = zyklenAnz + 1;
        int durchschnitt = laengeSum/zyklenAnz;
        String fertigeLaenge = String.valueOf(durchschnitt);
        editor.putString("laenge", fertigeLaenge);
        editor.putInt("laengeSum", laengeSum);
        editor.putInt("zyklenAnz", zyklenAnz);
        editor.commit();
    }

    public void neuePeriodeInPrefsSpeichern(String letztePeriode, SharedPreferences prefs) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("letztePeriode", letztePeriode);
        editor.commit();
    }
}