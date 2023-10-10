package com.example.tracker_ainura;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tracker_ainura.databinding.ActivityEinstellungenBinding;
import com.example.tracker_ainura.databinding.ActivityMainBinding;

import java.net.Inet4Address;

/**
 * Einstellungen-Activity:
 *
 * Hier kann die Perioden- und Zykluslänge geändert werden
 * */
public class EinstellungenActivity extends AppCompatActivity {

    private ActivityEinstellungenBinding binding;
    private String laengeMens;
    private String laengeZyklus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEinstellungenBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        SharedPreferences prefs = getApplicationContext().getSharedPreferences("SharedPrefs", Context.MODE_PRIVATE);
        String laenge = prefs.getString("laenge", "");
        String laengeMens = prefs.getString("laengeMens", "");

        setUpBtns(laenge, laengeMens);
    }

    /**
     * Methode, die den beiden Btns Clicklistener gibt (welche dann zu jeweiligen Methoden weiterführen)
     * */
    private void setUpBtns(String laenge, String laengeMens) {
        binding.btnLaengePeriodeChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                laengePeriodeDialog(laenge);
            }
        });

        binding.btnLaengeZyklusChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                laengeZyklusDialog(laengeMens);
            }
        });
    }

    /**
     * Methode, die neue Periodenlänge entgegennimmt (-> Dialog). Kontrolliert und speichert Wert
     * */
    private void laengePeriodeDialog(String laenge) {
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
                }else if(Integer.parseInt(laengeMens)>8||Integer.parseInt(laengeMens)<3){
                    Toast.makeText(getApplicationContext(), "Periodenlänge invalid", Toast.LENGTH_LONG).show();
                }else if(Integer.parseInt(laengeZyklus)<Integer.parseInt(laengeMens)){
                    Toast.makeText(getApplicationContext(), "Zyklus darf nicht kürzer als Periode sein", Toast.LENGTH_LONG).show();
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

    /**
     * Methode, die Menu zeigt
     * */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    /**
     * Methode, die ausgewähltes Menu registriert und zu jeweiliger Activity weiterleitet
     * */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.item_wissen:
                Intent wissenIntent = new Intent(EinstellungenActivity.this, WissenActivity.class);
                startActivity(wissenIntent);
                return true;
            case R.id.item_zusammenfassung:
                Intent zusammenfassungIntent = new Intent(EinstellungenActivity.this, ZusammenfassungActivity.class);
                zusammenfassungIntent.putExtra("ausNeuerPeriode", false);
                startActivity(zusammenfassungIntent);
                return true;
            case R.id.item_tagebuch_notiz:
                Intent notizenIntent = new Intent(EinstellungenActivity.this, TagebuchActivity.class);
                startActivity(notizenIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Methode, die neue Zykluslänge entgegennimmt (-> Dialog). Kontrolliert und speichert Wert
     * */
    private void laengeZyklusDialog(String laengeMens) {
        AlertDialog.Builder laengeDialog = new AlertDialog.Builder(EinstellungenActivity.this);
        laengeDialog.setTitle("Neue Zykluslänge eingeben: ");

        final EditText laengeInput = new EditText(EinstellungenActivity.this);
        laengeInput.setInputType(InputType.TYPE_CLASS_NUMBER);
        laengeDialog.setView(laengeInput);
        laengeDialog.setPositiveButton("Speichern", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                laengeZyklus = laengeInput.getText().toString();
                SharedPreferences prefs = getApplicationContext().getSharedPreferences("SharedPrefs", Context.MODE_PRIVATE);
                String laengeMens = prefs.getString("laengeMens", "");
                if(laengeZyklus.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Nice try. Aber es werden keine leeren Eingaben gespeichert.", Toast.LENGTH_LONG).show();
                }else if(Integer.parseInt(laengeZyklus)<15||Integer.parseInt(laengeZyklus)>45) {
                    Toast.makeText(getApplicationContext(), "Länge Zyklus invalid", Toast.LENGTH_LONG).show();
                }else if(Integer.parseInt(laengeZyklus)<Integer.parseInt(laengeMens)){
                    Toast.makeText(getApplicationContext(), "Zyklus darf nicht kürzer als Periode sein", Toast.LENGTH_LONG).show();
                }
                else{
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("laenge", laengeZyklus);
                    editor.commit();
                    Toast.makeText(getApplicationContext(), "Neue Zykluslänge wurde gespeichert", Toast.LENGTH_LONG).show();
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
}