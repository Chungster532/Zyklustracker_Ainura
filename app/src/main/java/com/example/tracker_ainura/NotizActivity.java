package com.example.tracker_ainura;

import static com.example.tracker_ainura.TagebuchActivity.arrayAdapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tracker_ainura.databinding.ActivityNotizBinding;

import java.util.HashSet;

public class NotizActivity extends AppCompatActivity {

    private ActivityNotizBinding binding;
    int notizId;
    String notiz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNotizBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.buttonNotizSave.setOnClickListener(view1 -> {
            String datum = binding.editTextDatum.getText().toString();
            String training = binding.editTextTraining.getText().toString();
            String blutung = binding.editTextBlutung.getText().toString();
            String stimmung = binding.editTextStimmung.getText().toString();
            String gewicht = binding.editTextGewicht.getText().toString();
            String sonstiges = binding.editTextSonstiges.getText().toString();

            if(datum.isEmpty() || training.isEmpty() || blutung.isEmpty() || stimmung.isEmpty() || gewicht.isEmpty() || sonstiges.isEmpty()){
                Toast.makeText(NotizActivity.this, "Bitte fülle alle Felder aus", Toast.LENGTH_LONG).show();
            }else{
                TagebuchActivity.notizen.add("");
                notizId = TagebuchActivity.notizen.size()-1;
                TagebuchActivity.arrayAdapter.notifyDataSetChanged();
                notiz = datum+
                        "\nTraining: "+training+
                        "\nBlutung: "+blutung+
                        "\nStimmung: "+stimmung+
                        "\nGewicht: "+gewicht+
                        "\nSonstiges: "+sonstiges;
                TagebuchActivity.notizen.set(notizId, notiz);
                TagebuchActivity.arrayAdapter.notifyDataSetChanged();

                SharedPreferences sharedPreferences = getApplication().getSharedPreferences("com.example.tracker_ainura", Context.MODE_PRIVATE);
                HashSet<String> set = new HashSet(TagebuchActivity.notizen);
                sharedPreferences.edit().putStringSet("notizen", set).apply();

                finish();
            }
        });
    }
}