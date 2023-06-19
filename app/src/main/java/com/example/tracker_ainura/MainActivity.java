package com.example.tracker_ainura;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.tracker_ainura.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        setUpBottomNav();
    }

    private void setUpBottomNav() {
        binding.bottomNav.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.nav_start:
                    return true;
                case R.id.nav_einstellungen:
                    Intent einstellungenActivityIntent = new Intent(MainActivity.this, EinstellungenActivity.class);
                    startActivity(einstellungenActivityIntent);
                    return true;
                case R.id.nav_notiz:
                    Intent notizActivityIntent = new Intent(MainActivity.this, NotizActivity.class);
                    startActivity(notizActivityIntent);
                    return true;
                case R.id.nav_wissen:
                    Intent wissenActivityIntent = new Intent(MainActivity.this, WissenActivity.class);
                    startActivity(wissenActivityIntent);
                    return true;
                case R.id.nav_zusammenfassung:
                    Intent zusammenfassungActivityIntent = new Intent(MainActivity.this, ZusammenfassungActivity.class);
                    startActivity(zusammenfassungActivityIntent);
                    return true;
            }
            return false;
        });
    }
}