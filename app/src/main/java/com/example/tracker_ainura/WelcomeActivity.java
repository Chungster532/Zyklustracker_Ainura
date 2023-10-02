package com.example.tracker_ainura;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.tracker_ainura.databinding.ActivityWelcomeBinding;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class WelcomeActivity extends AppCompatActivity {

    private ActivityWelcomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWelcomeBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.buttonStarten.setOnClickListener(mainActivitystart());
    }

    private View.OnClickListener mainActivitystart() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String laenge = binding.editTextLNge.getText().toString();
                String laengeMens = binding.editTextLNgeMens.getText().toString();
                int tag = binding.datePicker1.getDayOfMonth();
                String monatStrFertig;
                String tagStrFertig;
                String jahr = Integer.toString(binding.datePicker1.getYear());
                int monat = binding.datePicker1.getMonth();
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
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                LocalDate heute = LocalDate.now();
                String letztePeriode = tagStrFertig+"-"+monatStrFertig+"-"+jahr;
                LocalDate date1 = LocalDate.parse(letztePeriode, formatter );
                if (laenge.isEmpty() || laengeMens.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Bitte fülle alle Felder aus", Toast.LENGTH_LONG).show();
                }else if (Integer.parseInt(laenge)<Integer.parseInt(laengeMens)){
                    Toast.makeText(getApplicationContext(), "Die Periode darf nicht länger als der Zyklus sein", Toast.LENGTH_LONG).show();
                }else if(Integer.parseInt(laenge)<20 || Integer.parseInt(laenge)>45){
                    Toast.makeText(getApplicationContext(), "Länge Zyklus invalid", Toast.LENGTH_LONG).show();
                }else if(Integer.parseInt(laengeMens)>8 || Integer.parseInt(laengeMens)<3){
                    Toast.makeText(getApplicationContext(), "Länge Periode invalid", Toast.LENGTH_LONG).show();
                }else if(date1.isAfter(heute)){
                    Toast.makeText(WelcomeActivity.this, "Es darf kein zukünftiges Datum ausgewählt werden", Toast.LENGTH_SHORT).show();
                }
                else {
                    istPeriode(laengeMens, letztePeriode);
                    erstePeriodeSpeichern(laenge, laengeMens, letztePeriode);
                    SharedPreferences prefs = getApplicationContext().getSharedPreferences("SharedPrefs", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putBoolean("FirstTimeRun", false);
                    editor.putInt("laengeSum", 0);
                    editor.putInt("zyklenAnz", 0);
                    editor.apply();
                    Intent startenIntent = new Intent(WelcomeActivity.this, MainActivity.class);
                    startActivity(startenIntent);
                    finish();
                }
            }
        };
    }

    private void istPeriode(String laengeMens, String letztePeriode) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate date1 = LocalDate.parse(letztePeriode, formatter);
        LocalDate date2 = LocalDate.now();
        long daysBetween = ChronoUnit.DAYS.between(date1, date2.plusDays(1));
        SharedPreferences prefs = getApplicationContext().getSharedPreferences("SharedPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        if(daysBetween<8){
            editor.putBoolean("Periode", true);
            editor.commit();
        }else{
            editor.putBoolean("Periode", false);
            editor.commit();
        }
    }

    public void erstePeriodeSpeichern(String laenge, String laengeMens, String letztePeriode) {
        SharedPreferences prefs = getApplicationContext().getSharedPreferences("SharedPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("letztePeriode", letztePeriode);
        editor.commit();
        editor.putString("laenge", laenge);
        editor.commit();
        editor.putString("laengeMens", laengeMens);
        editor.commit();
    }
}