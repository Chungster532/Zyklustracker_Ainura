package com.example.tracker_ainura;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.tracker_ainura.Adapters.ZyklenListeAdapter;
import com.example.tracker_ainura.Database.RoomDB;
import com.example.tracker_ainura.Models.Zyklen;
import com.example.tracker_ainura.databinding.ActivityZusammenfassungBinding;

import java.util.ArrayList;
import java.util.List;

public class ZusammenfassungActivity extends AppCompatActivity {

    private ActivityZusammenfassungBinding binding;
    ZyklenListeAdapter zyklenListeAdapter;
    List<Zyklen> zyklen = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityZusammenfassungBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        RoomDB database_zyklen = RoomDB.getInstance(this);
        zyklen = database_zyklen.zyklenDao().getAll();
        zyklen.clear();
        zyklen.addAll(database_zyklen.zyklenDao().getAll());
        zyklenListeAdapter = new ZyklenListeAdapter(ZusammenfassungActivity.this, zyklen);
        zyklenListeAdapter.notifyDataSetChanged();

        setMensLaenge();

        updateRecycler(zyklen, zyklenListeAdapter);

        binding.buttonTagebuch.setOnClickListener(view1 -> {
            Intent tagebuchActivity = new Intent(ZusammenfassungActivity.this, TagebuchActivity.class);
            startActivity(tagebuchActivity);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.item_einstellungen:
                Intent einstellungenIntent = new Intent(ZusammenfassungActivity.this, EinstellungenActivity.class);
                startActivity(einstellungenIntent);
                return true;
            case R.id.item_wissen:
                Intent wissenIntent = new Intent(ZusammenfassungActivity.this, WissenActivity.class);
                startActivity(wissenIntent);
                return true;
            case R.id.item_tagebuch_notiz:
                Intent notizenIntent = new Intent(ZusammenfassungActivity.this, TagebuchActivity.class);
                startActivity(notizenIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setMensLaenge() {
        SharedPreferences prefs = getApplicationContext().getSharedPreferences("SharedPrefs", Context.MODE_PRIVATE);
        String laenge = prefs.getString("laenge", "");
        binding.textviewLaengeDurchschnitt.setText("Durchschnittliche Länge Zyklus: "+laenge);
    }

    private void updateRecycler(List<Zyklen> zyklen, ZyklenListeAdapter zyklenListeAdapter) {
        binding.recyclerviewZyklen.setHasFixedSize(true);
        binding.recyclerviewZyklen.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerviewZyklen.setAdapter(zyklenListeAdapter);
    }
}