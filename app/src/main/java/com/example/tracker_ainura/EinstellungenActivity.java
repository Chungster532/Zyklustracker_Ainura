package com.example.tracker_ainura;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tracker_ainura.databinding.ActivityEinstellungenBinding;
import com.example.tracker_ainura.databinding.ActivityMainBinding;

public class EinstellungenActivity extends AppCompatActivity {

    private ActivityEinstellungenBinding binding;
    private String laengeMens;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEinstellungenBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.btnLaengeChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder laengeDialog = new AlertDialog.Builder(EinstellungenActivity.this);
                laengeDialog.setTitle("Neue Periodenlänge eingeben: ");

                final EditText laengeInput = new EditText(EinstellungenActivity.this);
                laengeInput.setInputType(InputType.TYPE_CLASS_NUMBER);
                laengeDialog.setView(laengeInput);
                laengeDialog.setPositiveButton("Speichern", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        laengeMens = laengeInput.getText().toString();
                        if(laengeMens.isEmpty()){
                            Toast.makeText(getApplicationContext(), "Nice try. Aber es werden keine leeren Eingaben gespeichert.", Toast.LENGTH_LONG).show();
                        }else{
                            SharedPreferences prefs = getApplicationContext().getSharedPreferences("SharedPrefs", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putString("laengeMens", laengeMens);
                            editor.commit();
                            Toast.makeText(getApplicationContext(), "Neue Periodenlänge wurde gespeichert", Toast.LENGTH_LONG).show();
                            finish();
                        }
                    }
                });

                laengeDialog.setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                laengeDialog.show();
            }
        });
    }
}