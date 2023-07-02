package com.example.tracker_ainura;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Toast;

import com.example.tracker_ainura.databinding.ActivityMainBinding;
import com.example.tracker_ainura.databinding.ActivityWelcomeBinding;

public class WelcomeActivity extends AppCompatActivity {

    private ActivityWelcomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWelcomeBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        SharedPreferences prefs = getSharedPreferences("PREFERENCE", MODE_PRIVATE);
        boolean firstTime = prefs.getBoolean("FirstTimeRun", true);
        SharedPreferences userData = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if(firstTime){
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("FirstTimeRun", false);
            editor.apply();
            binding.buttonStarten.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String laenge = binding.editTextLNge.getText().toString();
                    String laengeMens = binding.editTextLNgeMens.getText().toString();
                    String tag = Integer.toString(binding.datePicker1.getDayOfMonth());
                    String monat = Integer.toString(binding.datePicker1.getMonth());
                    String jahr = Integer.toString(binding.datePicker1.getYear());
                    if(laenge.isEmpty()||laengeMens.isEmpty()){
                        Toast.makeText(getApplicationContext(), "Bitte fülle alle Felder aus", Toast.LENGTH_LONG).show();
                    }else{
                        SharedPreferences.Editor editor = userData.edit();
                        editor.putString("laenge", laenge);
                        editor.commit();
                        editor.putString("laengeMens", laengeMens);
                        editor.commit();
                        editor.putString("tag", tag);
                        editor.commit();
                        editor.putString("monat", monat);
                        editor.commit();
                        editor.putString("jahr", jahr);
                        editor.commit();
                        Intent startenIntent = new Intent(WelcomeActivity.this, MainActivity.class);
                        startActivity(startenIntent);
                    }
                }
            });
        }else{
            Intent welcomeIntent = new Intent(WelcomeActivity.this, MainActivity.class);
            startActivity(welcomeIntent);
        }
    }
}