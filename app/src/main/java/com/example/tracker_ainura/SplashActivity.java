package com.example.tracker_ainura;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        SharedPreferences prefs = getSharedPreferences("SharedPrefs", MODE_PRIVATE);
        boolean firstTimeRun = prefs.getBoolean("FirstTimeRun", true);

        new Timer().schedule(
                new TimerTask(){

                    @Override
                    public void run(){
                        if(firstTimeRun){
                            Intent welcomeIntent = new Intent(SplashActivity.this, WelcomeActivity.class);
                            startActivity(welcomeIntent);
                        }else{
                            Intent mainActivityIntent = new Intent(SplashActivity.this, MainActivity.class);
                            startActivity(mainActivityIntent);
                        }
                        finish();
                    }

                }, 1500);

    }
}