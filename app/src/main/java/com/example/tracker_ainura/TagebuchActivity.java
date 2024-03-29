package com.example.tracker_ainura;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.tracker_ainura.Adapters.NotizenListeAdapter;
import com.example.tracker_ainura.Database.RoomDB;
import com.example.tracker_ainura.Models.Notizen;
import com.example.tracker_ainura.databinding.ActivityTagebuchBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Tagebuch-Activity:
 *
 * zeigt alle bisherigen Notizen (mit RecyclerView NotizenListeAdapter) auf.
 * Suchfunktion in Notizen.
 * Leitet zu NotizActivity weiter.
 * */
public class TagebuchActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {
    private ActivityTagebuchBinding binding;
    NotizenListeAdapter notizenListeAdapter;
    List<Notizen> notizen = new ArrayList<>();
    RoomDB database;
    Notizen selectedNotiz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTagebuchBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        database = RoomDB.getInstance(this);
        notizen = database.notizenDao().getAll();

        updateRecycler(notizen);

        setUpNotizBtn();

        setUpSearchBar();
    }

    /**
     * Methode, die eingegebenen String entgegennimmt und an Filter-Funktion weiterleitet
     * */
    private void setUpSearchBar() {
        binding.searchviewNotizen.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                filter(s);
                return true;
            }
        });
    }

    /**
     * Methode, die Text für Searchbar filtert
     *
     * @param newText Text aus Searchbar (mit jeder Änderung der Eingabe wird dieser neu eingereicht)
     * */
    private void filter(String newText){
        List<Notizen> filteredList = new ArrayList<>();
        for (Notizen singleNote : notizen){
            String wholeNote = singleNote.getBlutung()+singleNote.getDatum()+singleNote.getSonstiges()+singleNote.getStimmung()+singleNote.getTraining();
            if (wholeNote.toLowerCase().contains(newText.toLowerCase(Locale.ROOT))){
                filteredList.add(singleNote);
            };
        }
        notizenListeAdapter.filterList(filteredList);
    }

    /**
     * Methode, die Clicklistener für Btn zum Erstellen der Notiz macht
     * */
    private void setUpNotizBtn() {
        binding.buttonNotizErstellen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent notizErstellen = new Intent(TagebuchActivity.this, NotizActivity.class);
                startActivityForResult(notizErstellen, 101);//101 zum Erstellen, 102 zum Bearbeiten
            }
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
                Intent einstellungenIntent = new Intent(TagebuchActivity.this, EinstellungenActivity.class);
                startActivity(einstellungenIntent);
                return true;
            case R.id.item_wissen:
                Intent wissenIntent = new Intent(TagebuchActivity.this, WissenActivity.class);
                startActivity(wissenIntent);
                return true;
            case R.id.item_zusammenfassung:
                Intent zusammenfassungIntent = new Intent(TagebuchActivity.this, ZusammenfassungActivity.class);
                zusammenfassungIntent.putExtra("ausNeuerPeriode", false);
                startActivity(zusammenfassungIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Methode, die kontrolliert, ob Notiz, welche gerade in NotizActivity erstellt wurde, neue oder bearbeitete ist.
     * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==101){ // neue Notiz
            if (resultCode== Activity.RESULT_OK){
                Notizen neueNotiz = (Notizen) data.getSerializableExtra("notiz");
                database.notizenDao().insert(neueNotiz);
                notizen.clear();
                notizen.addAll(database.notizenDao().getAll());
                notizenListeAdapter.notifyDataSetChanged();
            }
        } else if (requestCode==102){ // alte Notiz
            if(resultCode==Activity.RESULT_OK){
                Notizen neue_notiz = (Notizen) data.getSerializableExtra("notiz");
                database.notizenDao().update(neue_notiz.getId(), neue_notiz.getDatum(), neue_notiz.getTagZyklus(), neue_notiz.getTraining(), neue_notiz.getBlutung(), neue_notiz.getStimmung(), neue_notiz.getSonstiges());
                notizen.clear();
                notizen.addAll(database.notizenDao().getAll());
                notizenListeAdapter.notifyDataSetChanged();
            }
        }
    }

    /**
     * Methode, die RecyclerView updatet
     * */
    private void updateRecycler(List<Notizen> notizen) {
        binding.recyclerviewTagebuch.setHasFixedSize(true);
        binding.recyclerviewTagebuch.setLayoutManager(new LinearLayoutManager(this));
        notizenListeAdapter = new NotizenListeAdapter(TagebuchActivity.this, notizen, notizenClickListener);
        binding.recyclerviewTagebuch.setAdapter(notizenListeAdapter);
    }

    /**
     * Methode, die das Anklicken von Notizen regelt (kurzer Klick: bearbeiten, langer Klick: löschen)
     * */
    private final NotizenClickListener notizenClickListener = new NotizenClickListener() {
        @Override
        public void onClick(Notizen notiz) {
            Intent notizBearbeiten = new Intent(TagebuchActivity.this, NotizActivity.class);
            notizBearbeiten.putExtra("alte_notiz", notiz);
            startActivityForResult(notizBearbeiten, 102);

        }

        @Override
        public void onLongClick(Notizen notiz, CardView cardview) {
            selectedNotiz = new Notizen();
            selectedNotiz = notiz;
            showPopup(cardview);

        }
    };

    /**
     * Methode, die Popup zum Löschen der Notiz anzeigt
     * */
    private void showPopup(CardView cardview) {
        PopupMenu popupMenu = new PopupMenu(this, cardview);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.inflate(R.menu.popup_menu);
        popupMenu.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.delete:
                database.notizenDao().delete(selectedNotiz);
                notizen.remove(selectedNotiz);
                notizenListeAdapter.notifyDataSetChanged();
                Toast.makeText(this, "Notiz wurde gelöscht", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return false;
        }
    }
}