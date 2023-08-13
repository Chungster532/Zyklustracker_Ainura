package com.example.tracker_ainura;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tracker_ainura.Models.Notizen;
import com.example.tracker_ainura.databinding.ActivityMainBinding;
import com.example.tracker_ainura.databinding.ActivityNotizBinding;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;

public class NotizActivity extends AppCompatActivity {

    private ActivityNotizBinding binding;
    Notizen notiz;
    boolean istAlteNotiz = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNotizBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        notiz = new Notizen();
        try{
            notiz = (Notizen) getIntent().getSerializableExtra("alte_notiz");
            String datum = notiz.getDatum();
            String[] teile = datum.split("-");
            int tag = Integer.parseInt(teile[0]);
            int monat = Integer.parseInt(teile[1])-1;
            int jahr = Integer.parseInt(teile[2]);
            binding.datepickerDatum.updateDate(monat, jahr, tag);
            binding.editTextTraining.setText(notiz.getTraining());
            binding.editTextBlutung.setText(notiz.getBlutung());
            binding.editTextStimmung.setText(notiz.getStimmung());
            binding.editTextSonstiges.setText(notiz.getSonstiges());
            istAlteNotiz = true;
        }catch (Exception e){
            e.printStackTrace();
        }

        binding.btnNotizspeichern.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notizSpeichern();
            }
        });
    }

    private void notizSpeichern() {
        String datum = getDate();
        String training = binding.editTextTraining.getText().toString();
        String blutung = binding.editTextBlutung.getText().toString();
        String stimmung = binding.editTextStimmung.getText().toString();
        String sonstiges = binding.editTextSonstiges.getText().toString();
        String tagZyklus = tagZyklusAusrechnen(datum);

        if (training.isEmpty() || blutung.isEmpty() || stimmung.isEmpty() || sonstiges.isEmpty()){
            Toast.makeText(NotizActivity.this, "Bitte fülle alle Felder aus", Toast.LENGTH_LONG).show();
        } else {
            if (!istAlteNotiz){
                notiz = new Notizen();
            }

            notiz.setDatum(datum);
            notiz.setTraining(training);
            notiz.setBlutung(blutung);
            notiz.setStimmung(stimmung);
            notiz.setSonstiges(sonstiges);
            notiz.setTagZyklus(tagZyklus);

            Intent intent = new Intent(NotizActivity.this, TagebuchActivity.class);
            intent.putExtra("notiz", notiz);
            setResult(Activity.RESULT_OK, intent);
            finish();
        }
    }

    private String getDate() {
        int tag = binding.datepickerDatum.getDayOfMonth();
        String jahr = Integer.toString(binding.datepickerDatum.getYear());
        int monat = binding.datepickerDatum.getMonth();
        String monatStrFertig;
        String tagStrFertig;
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
        return tagStrFertig+"-"+monatStrFertig+"-"+jahr;
    }

    private String tagZyklusAusrechnen(String datum) {
        SharedPreferences prefs = getApplicationContext().getSharedPreferences("SharedPrefs", Context.MODE_PRIVATE);
        String letztePeriode = prefs.getString("letztePeriode", "");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate date1 = LocalDate.parse(letztePeriode, formatter);
        LocalDate date2 = LocalDate.parse(datum, formatter);
        long daysBetween = ChronoUnit.DAYS.between(date1, date2.plusDays(1));
        int daysBetween1 = (int) daysBetween;
        return String.valueOf(daysBetween1);
    }
}