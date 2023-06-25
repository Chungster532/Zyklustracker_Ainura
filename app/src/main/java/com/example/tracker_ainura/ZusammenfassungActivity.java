package com.example.tracker_ainura;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.tracker_ainura.databinding.ActivityZusammenfassungBinding;

public class ZusammenfassungActivity extends AppCompatActivity {

    private ActivityZusammenfassungBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityZusammenfassungBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.buttonTagebuch.setOnClickListener(view1 -> {
            Intent tagebuchActivity = new Intent(ZusammenfassungActivity.this, TagebuchActivity.class);
            startActivity(tagebuchActivity);
        });
    }
}