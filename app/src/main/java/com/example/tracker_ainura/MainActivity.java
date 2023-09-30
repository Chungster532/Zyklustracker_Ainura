package com.example.tracker_ainura;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
import com.example.tracker_ainura.databinding.ActivityMainBinding;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    RoomDB database_zyklen;
    ZyklenListeAdapter zyklenListeAdapter;
    List<Zyklen> zyklen = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        database_zyklen = RoomDB.getInstance(this);
        zyklen = database_zyklen.zyklenDao().getAll();

        SharedPreferences prefs = getApplicationContext().getSharedPreferences("SharedPrefs", Context.MODE_PRIVATE);

        String strLaenge = prefs.getString("laenge", "");
        String strLaengeMens = prefs.getString("laengeMens", "");
        int laenge = Integer.parseInt(strLaenge);
        int laengeMens = Integer.parseInt(strLaengeMens);
        String letztePeriode = prefs.getString("letztePeriode", "");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate date1 = LocalDate.parse(letztePeriode, formatter);

        ausrechnen(date1, laengeMens, laenge);
        setUpBtn();

    }

    private void setUpBtn() {
        SharedPreferences prefs = getSharedPreferences("SharedPrefs", MODE_PRIVATE);
        boolean periode = prefs.getBoolean("Periode", false);
        if(periode){
            binding.buttonErsterTag.setText("Ende der Periode?");
            binding.buttonErsterTag.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent endePeriodeIntent = new Intent(MainActivity.this, EndePeriodeActivity.class);
                    startActivity(endePeriodeIntent);
                }
            });
        }else{
            binding.buttonErsterTag.setText("1. Tag der Periode?");
            binding.buttonErsterTag.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent neuePeriodeIntent = new Intent(MainActivity.this, NeuePeriodeActivity.class);
                    startActivity(neuePeriodeIntent);
                }
            });
        }
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
                Intent einstellungenIntent = new Intent(MainActivity.this, EinstellungenActivity.class);
                startActivity(einstellungenIntent);
                return true;
            case R.id.item_wissen:
                Intent wissenIntent = new Intent(MainActivity.this, WissenActivity.class);
                startActivity(wissenIntent);
                return true;
            case R.id.item_zusammenfassung:
                Intent zusammenfassungIntent = new Intent(MainActivity.this, ZusammenfassungActivity.class);
                zusammenfassungIntent.putExtra("ausNeuerPeriode", false);
                startActivity(zusammenfassungIntent);
                return true;
            case R.id.item_tagebuch_notiz:
                Intent notizenIntent = new Intent(MainActivity.this, TagebuchActivity.class);
                startActivity(notizenIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
    private void ausrechnen(LocalDate date1, int laengeMens, int zyklus) {
        LocalDate date2 = LocalDate.now();
        long daysBetween = ChronoUnit.DAYS.between(date1, date2.plusDays(1));
        DateTimeFormatter df = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        if (daysBetween <= laengeMens) {
            String m1 = df.format(date1.plusDays(zyklus));
            String m2 = df.format(date1.plusDays(zyklus * 2));
            String m3 = df.format(date1.plusDays(zyklus * 3));
            String f1 = df.format(date1.plusDays(laengeMens));
            String f2 = df.format(date1.plusDays(laengeMens+zyklus));
            String f3 = df.format(date1.plusDays(laengeMens+zyklus*2));
            String o1 = df.format(date1.plusDays(zyklus/2));
            String o2 = df.format(date1.plusDays(zyklus/2+zyklus));
            String o3 = df.format(date1.plusDays(zyklus/2+zyklus*2));
            String l1 = df.format(date1.plusDays(zyklus/2+3));
            String l2 = df.format(date1.plusDays(zyklus/2+3+zyklus));
            String l3 = df.format(date1.plusDays(zyklus/2+3+zyklus*2));
            binding.textviewPhase.setText(daysBetween + ". Tag: Menstruationsphase");
            binding.textviewTrainingsempfehlung.setText("Niedrigere Intensität (z.B. lockeres Ausdauertraining, Yoga, Stretching). Hartes Krafttraining kann schaden, weil schützendes Estradiol fehlt");
            binding.textview4.setText("Nächste Menstruationsphase:\n" + m1 + ", " + m2 + ", " + m3);
            binding.textview1.setText("Nächste Follikelphase:\n" + f1 + ", " + f2 + ", " + f3);
            binding.textview2.setText("Nächste Ovulationsphase:\n" + o1 + ", " + o2 + ", " + o3);
            binding.textview3.setText("Nächste Lutealphase:\n" + l1 + ", " + l2 + ", " + l3);
        } else if (daysBetween <= 7 + laengeMens) {
            String m1 = df.format(date1.plusDays(zyklus));
            String m2 = df.format(date1.plusDays(zyklus * 2));
            String m3 = df.format(date1.plusDays(zyklus * 3));
            String f1 = df.format(date1.plusDays(laengeMens+zyklus));
            String f2 = df.format(date1.plusDays(laengeMens+zyklus*2));
            String f3 = df.format(date1.plusDays(laengeMens+zyklus*3));
            String o1 = df.format(date1.plusDays(zyklus/2));
            String o2 = df.format(date1.plusDays(zyklus/2+zyklus));
            String o3 = df.format(date1.plusDays(zyklus/2+zyklus*2));
            String l1 = df.format(date1.plusDays(zyklus/2+3));
            String l2 = df.format(date1.plusDays(zyklus/2+3+zyklus));
            String l3 = df.format(date1.plusDays(zyklus/2+3+zyklus*2));
            binding.textviewPhase.setText(daysBetween + ". Tag: Follikelphase");
            binding.textviewTrainingsempfehlung.setText("Perfekter Zeitpunkt für Krafttraining: Trainingseffekt besonders hoch, da Estradiol Muskulatur stärker auf Trainingsreize reagieren lässt.");
            binding.textview1.setText("Nächste Menstruationsphase:\n" + m1 + ", " + m2 + ", " + m3);
            binding.textview4.setText("Nächste Follikelphase:\n" + f1 + ", " + f2 + ", " + f3);
            binding.textview2.setText("Nächste Ovulationsphase:\n" + o1 + ", " + o2 + ", " + o3);
            binding.textview3.setText("Nächste Lutealphase:\n" + l1 + ", " + l2 + ", " + l3);
        } else if (daysBetween <= 16) {
            String m1 = df.format(date1.plusDays(zyklus));
            String m2 = df.format(date1.plusDays(zyklus * 2));
            String m3 = df.format(date1.plusDays(zyklus * 3));
            String f1 = df.format(date1.plusDays(laengeMens+zyklus));
            String f2 = df.format(date1.plusDays(laengeMens+zyklus*2));
            String f3 = df.format(date1.plusDays(laengeMens+zyklus*3));
            String o1 = df.format(date2.plusDays(zyklus));
            String o2 = df.format(date2.plusDays(zyklus*2));
            String o3 = df.format(date2.plusDays(zyklus*3));
            String l1 = df.format(date1.plusDays(zyklus/2+3));
            String l2 = df.format(date1.plusDays(zyklus/2+3+zyklus));
            String l3 = df.format(date1.plusDays(zyklus/2+3+zyklus*2));
            binding.textviewPhase.setText(daysBetween + ". Tag: Ovulation");
            binding.textviewTrainingsempfehlung.setText("Auf Körper hören");
            binding.textview1.setText("Nächste Menstruationsphase:\n" + m1 + ", " + m2 + ", " + m3);
            binding.textview3.setText("Nächste Follikelphase:\n" + f1 + ", " + f2 + ", " + f3);
            binding.textview4.setText("Nächste Ovulationsphase:\n" + o1 + ", " + o2 + ", " + o3);
            binding.textview2.setText("Nächste Lutealphase:\n" + l1 + ", " + l2 + ", " + l3);
        } else if (daysBetween <= zyklus) {
            String m1 = df.format(date1.plusDays(zyklus));
            String m2 = df.format(date1.plusDays(zyklus * 2));
            String m3 = df.format(date1.plusDays(zyklus * 3));
            String f1 = df.format(date1.plusDays(laengeMens+zyklus));
            String f2 = df.format(date1.plusDays(laengeMens+zyklus*2));
            String f3 = df.format(date1.plusDays(laengeMens+zyklus*3));
            String o1 = df.format(date1.plusDays(zyklus/2+zyklus));
            String o2 = df.format(date1.plusDays(zyklus/2+zyklus*2));
            String o3 = df.format(date1.plusDays(zyklus/2+zyklus*3));
            String l1 = df.format(date1.plusDays(zyklus/2+3+zyklus));
            String l2 = df.format(date1.plusDays(zyklus/2+3+zyklus*2));
            String l3 = df.format(date1.plusDays(zyklus/2+3+zyklus*3));
            binding.textviewPhase.setText(daysBetween + ". Tag: Lutealphase");
            binding.textviewTrainingsempfehlung.setText("Trainingsintensität und -volumen reduzieren: leichteres Krafttraining, lockere Regenerationsläufe");
            binding.textview1.setText("Nächste Menstruationsphase:\n" + m1 + ", " + m2 + ", " + m3);
            binding.textview2.setText("Nächste Follikelphase:\n" + f1 + ", " + f2 + ", " + f3);
            binding.textview3.setText("Nächste Ovulationsphase:\n" + o1 + ", " + o2 + ", " + o3);
            binding.textview4.setText("Nächste Lutealphase:\n" + l1 + ", " + l2 + ", " + l3);
        } else if (daysBetween > zyklus) {
            binding.textviewPhase.setText((daysBetween - zyklus) + " Tage zu spät");
            binding.textviewTrainingsempfehlung.setText(" ");
            binding.textview1.setText(" ");
            binding.textview2.setText(" ");
            binding.textview3.setText(" ");
            binding.textview4.setText(" ");
        }
    }

}