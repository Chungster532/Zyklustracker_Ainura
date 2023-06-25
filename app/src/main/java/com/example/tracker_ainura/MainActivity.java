package com.example.tracker_ainura;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;

import com.example.tracker_ainura.databinding.ActivityMainBinding;

import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        SharedPreferences userData = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        int laenge = Integer.parseInt(userData.getString("laenge", "fail"));
        int laengeMens = Integer.parseInt(userData.getString("laengeMens", "fail"));
        int tag = Integer.parseInt(userData.getString("tag", "fail"));
        int monat = Integer.parseInt(userData.getString("tag", "fail"));
        int jahr = Integer.parseInt(userData.getString("tag", "fail"));

        ausrechnen(jahr, monat, tag, laengeMens, laenge);

        setUpBottomNav();
    }

    private void ausrechnen(int jahr, int monat, int tag, int laengeMens, int laenge) {
        Calendar heute = Calendar.getInstance();
        Calendar letztePeriode = Calendar.getInstance();
        letztePeriode.set(jahr, monat, tag);

        long end = heute.getTimeInMillis();
        long start = letztePeriode.getTimeInMillis();
        long daysBetween = TimeUnit.MILLISECONDS.toDays(Math.abs(end - start));
        if (daysBetween<=laengeMens){
            binding.textviewPhase.setText("Menstruation");
            binding.textviewTrainingsempfehlung.setText("Niedrigere Intensität (z.B. lockeres Ausdauertraining, Yoga, Stretching). Hartes Krafttraining kann schaden, weil schützendes Estradiol fehlt");
            heute.add(Calendar.DATE, laenge);
            binding.textview1.setText("Nächste Menstruationsblutung fängt am "+new SimpleDateFormat("dd-MM-yyyy").format(heute.getInstance().getTime())+" an");
            heute.add(Calendar.DATE, -12);
            binding.textview2.setText("Nächste Lutealphase fängt am "+new SimpleDateFormat("dd-MM-yyyy").format(heute.getInstance().getTime())+" an");
            heute.add(Calendar.DATE, -4);
            binding.textview3.setText("Nächste Ovulationsphase fängt am "+new SimpleDateFormat("dd-MM-yyyy").format(heute.getInstance().getTime())+" an");
            heute.add(Calendar.DATE, -8);
            binding.textview4.setText("Nächste Lutealphase fängt am "+new SimpleDateFormat("dd-MM-yyyy").format(heute.getInstance().getTime())+" an");
        } else if (daysBetween<=8+laengeMens){
            binding.textviewPhase.setText("Follikelphase");
            binding.textviewTrainingsempfehlung.setText("Perfekter Zeitpunkt für Krafttraining: Trainingseffekt besonders hoch, da Estradiol Muskulatur stärker auf Trainingsreize reagieren lässt.");
            heute.add(Calendar.DATE, laenge);
            binding.textview1.setText("Nächste Follikelphase fängt am "+new SimpleDateFormat("dd-MM-yyyy").format(heute.getInstance().getTime())+" an");
            heute.add(Calendar.DATE, -laengeMens);
            binding.textview2.setText("Nächste Menstruationsphase fängt am "+new SimpleDateFormat("dd-MM-yyyy").format(heute.getInstance().getTime())+" an");
            heute.add(Calendar.DATE, -12);
            binding.textview3.setText("Nächste Lutealphase fängt am "+new SimpleDateFormat("dd-MM-yyyy").format(heute.getInstance().getTime())+" an");
            heute.add(Calendar.DATE, -4);
            binding.textview4.setText("Nächste Ovulation fängt am "+new SimpleDateFormat("dd-MM-yyyy").format(heute.getInstance().getTime())+" an");
        } else if (daysBetween<=16){
            binding.textviewPhase.setText("Ovulation");
            binding.textviewTrainingsempfehlung.setText("Auf Körper hören");
            heute.add(Calendar.DATE, laenge);
            binding.textview1.setText("Nächste Ovulation fängt am "+new SimpleDateFormat("dd-MM-yyyy").format(heute.getInstance().getTime())+" an");
            heute.add(Calendar.DATE, -8);
            binding.textview2.setText("Nächste Follikelphase fängt am "+new SimpleDateFormat("dd-MM-yyyy").format(heute.getInstance().getTime())+" an");
            heute.add(Calendar.DATE, -laengeMens);
            binding.textview3.setText("Nächste Menstruationsphase fängt am "+new SimpleDateFormat("dd-MM-yyyy").format(heute.getInstance().getTime())+" an");
            heute.add(Calendar.DATE, -12);
            binding.textview4.setText("Nächste Lutealphase fängt am "+new SimpleDateFormat("dd-MM-yyyy").format(heute.getInstance().getTime())+" an");
        } else {
            binding.textviewPhase.setText("Lutealphase");
            binding.textviewTrainingsempfehlung.setText("Trainingsintensität und -volumen reduzieren: leichteres Krafttraining, lockere Regenerationsläufe");
            heute.add(Calendar.DATE, laenge);
            binding.textview1.setText("Nächste Lutealphase fängt am "+new SimpleDateFormat("dd-MM-yyyy").format(heute.getInstance().getTime())+" an");
            heute.add(Calendar.DATE, -4);
            binding.textview2.setText("Nächste Ovulation fängt am "+new SimpleDateFormat("dd-MM-yyyy").format(heute.getInstance().getTime())+" an");
            heute.add(Calendar.DATE, -8);
            binding.textview3.setText("Nächste Follikelphase fängt am "+new SimpleDateFormat("dd-MM-yyyy").format(heute.getInstance().getTime())+" an");
            heute.add(Calendar.DATE, -laengeMens);
            binding.textview4.setText("Nächste Menstruationsphase fängt am "+new SimpleDateFormat("dd-MM-yyyy").format(heute.getInstance().getTime())+" an");
        }
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
                    Intent notizActivityIntent = new Intent(MainActivity.this, TagebuchActivity.class);
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