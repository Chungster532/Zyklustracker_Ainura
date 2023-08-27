package com.example.tracker_ainura;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.tracker_ainura.databinding.ActivityWelcomeBinding;

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
                String letztePeriode = tagStrFertig+"-"+monatStrFertig+"-"+jahr;
                if (laenge.isEmpty() || laengeMens.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Bitte fülle alle Felder aus", Toast.LENGTH_LONG).show();
                }else if (Integer.parseInt(laenge)<Integer.parseInt(laengeMens)){
                    Toast.makeText(getApplicationContext(), "Die Periode darf nicht länger als der Zyklus sein", Toast.LENGTH_LONG).show();
                }else if(Integer.parseInt(laenge)>37 || Integer.parseInt(laengeMens)>8){
                    Toast.makeText(getApplicationContext(), "Zu grosse Werte", Toast.LENGTH_LONG).show();
                }
                else {
                    erstePeriodeSpeichern(laenge, laengeMens, letztePeriode);
                    SharedPreferences prefs = getApplicationContext().getSharedPreferences("SharedPrefs", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putBoolean("FirstTimeRun", false);
                    editor.apply();
                    Intent startenIntent = new Intent(WelcomeActivity.this, MainActivity.class);
                    startActivity(startenIntent);
                    finish();
                }
            }
        };
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