package com.example.tracker_ainura;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.tracker_ainura.databinding.ActivityEinstellungenBinding;
import com.example.tracker_ainura.databinding.ActivityNotizBinding;

public class NotizActivity extends AppCompatActivity {

    private ActivityNotizBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNotizBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
    }
}