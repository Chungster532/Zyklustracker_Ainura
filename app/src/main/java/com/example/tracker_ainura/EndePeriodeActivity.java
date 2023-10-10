package com.example.tracker_ainura;

import static java.time.LocalDate.parse;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.tracker_ainura.Database.RoomDB;
import com.example.tracker_ainura.Models.Zyklen;
import com.example.tracker_ainura.databinding.ActivityEndePeriodeBinding;
import com.example.tracker_ainura.databinding.ActivityNeuePeriodeBinding;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * EndePeriode-Activity:
 *
 * Hier wird die Periode beendet (Periode-Btn auf MainActivity führt hierhin)
 * */
public class EndePeriodeActivity extends AppCompatActivity {
    private ActivityEndePeriodeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEndePeriodeBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        setUpSpeichernBtn();
    }

    /**
     * Methode, die Clicklistener für Btn aufsetzt und zu speichern-Methode leitet
     * */
    private void setUpSpeichernBtn() {
        binding.btnSpeichern.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                endePeriodeSpeichern();
                finish();
            }
        });
    }

    /**
     * Methode, die eingegebenen Wert holt, kontrolliert und speichert (String laengeMens und Boolean Periode zu SharedPreferences)
     * */
    private void endePeriodeSpeichern() {
        SharedPreferences prefs = getApplicationContext().getSharedPreferences("SharedPrefs", Context.MODE_PRIVATE);
        int tag = binding.datepickerNeuePeriode.getDayOfMonth();
        String jahr = Integer.toString(binding.datepickerNeuePeriode.getYear());
        int monat = binding.datepickerNeuePeriode.getMonth();

        DateRetriever dr = new DateRetriever();
        String endePeriode = dr.convertDateFromDatePicker(tag, jahr, monat);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String anfangPeriode = prefs.getString("letztePeriode", "");
        LocalDate date1 = parse(anfangPeriode, formatter);
        LocalDate date2 = parse(endePeriode, formatter);
        LocalDate heute = LocalDate.now();

        long differenz = ChronoUnit.DAYS.between(date1, date2);
        if(differenz>8){
            Toast.makeText(this, "Periode kann nicht länger als 8 Tage dauern", Toast.LENGTH_SHORT).show();
        }else if(date2.isAfter(heute)){
            Toast.makeText(this, "Es darf kein zukünftiges Datum ausgewählt werden", Toast.LENGTH_SHORT).show();
        }else if(differenz<3){
            Toast.makeText(this, "Periode kann nicht kürzer als 3 Tage dauern", Toast.LENGTH_SHORT).show();
        }
        else{
            String fertigeLaenge = String.valueOf(differenz);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("laengeMens", fertigeLaenge);
            editor.putBoolean("Periode", false);
            editor.commit();
            Intent returnIntent = new Intent(EndePeriodeActivity.this, MainActivity.class);
            startActivity(returnIntent);
        }
    }
}