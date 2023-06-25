package com.example.tracker_ainura;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

import com.example.tracker_ainura.databinding.ActivityNotizBinding;
import com.example.tracker_ainura.databinding.ActivityTagebuchBinding;

import java.util.ArrayList;
import java.util.HashSet;

public class TagebuchActivity extends AppCompatActivity {

    static ArrayList<String> notizen = new ArrayList<>();
    static ArrayAdapter arrayAdapter;
    private ActivityTagebuchBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTagebuchBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        SharedPreferences sharedPreferences = getApplication().getSharedPreferences("com.example.tracker_ainura", Context.MODE_PRIVATE);
        HashSet<String> set = (HashSet<String>) sharedPreferences.getStringSet("notizen", null);
        if (set == null){
            notizen.add("Example note");
        }else{
            notizen = new ArrayList(set);
        }

        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1, notizen);
        binding.listView.setAdapter(arrayAdapter);

        binding.buttonNotizErstellen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent notizErstellenIntent = new Intent(getApplicationContext(), NotizActivity.class);
                startActivity(notizErstellenIntent);
            }
        });
        binding.listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                int itemToDelete = i;
                new AlertDialog.Builder(TagebuchActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Soll diese Notiz gelöscht werden?")
                        .setPositiveButton("Ja", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                notizen.remove(itemToDelete);
                                arrayAdapter.notifyDataSetChanged();

                                SharedPreferences sharedPreferences = getApplication().getSharedPreferences("com.example.tracker_ainura", Context.MODE_PRIVATE);
                                HashSet<String> set = new HashSet(TagebuchActivity.notizen);
                                sharedPreferences.edit().putStringSet("notizen", set).apply();
                            }
                        })
                        .setNegativeButton("Nein", null)
                        .show();
                return true;
            }
        });
    }
}