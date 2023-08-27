package com.example.tracker_ainura;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.applandeo.materialcalendarview.CalendarDay;
import com.applandeo.materialcalendarview.EventDay;
import com.example.tracker_ainura.Adapters.ZyklenListeAdapter;
import com.example.tracker_ainura.Database.RoomDB;
import com.example.tracker_ainura.Models.Zyklen;
import com.example.tracker_ainura.databinding.ActivityMainBinding;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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

        binding.buttonErsterTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent neuePeriodeIntent = new Intent(MainActivity.this, NeuePeriodeActivity.class);
                startActivity(neuePeriodeIntent);
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
        switch (item.getItemId()) {
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

        Calendar min = Calendar.getInstance();
        min.add(Calendar.DATE, -1);
        binding.calenderviewPhasen.setMinimumDate(min);

        LocalDate date2 = LocalDate.now();
        long daysBetween = ChronoUnit.DAYS.between(date1, date2.plusDays(1));
        ZoneId zoneId = ZoneId.systemDefault();

        if (daysBetween <= zyklus) {
            Calendar m1Cal = Calendar.getInstance();
            Calendar m2Cal = Calendar.getInstance();
            Calendar m3Cal = Calendar.getInstance();
            Calendar f1Cal = Calendar.getInstance();
            Calendar f2Cal = Calendar.getInstance();
            Calendar f3Cal = Calendar.getInstance();
            Calendar o1Cal = Calendar.getInstance();
            Calendar o2Cal = Calendar.getInstance();
            Calendar o3Cal = Calendar.getInstance();
            Calendar l1Cal = Calendar.getInstance();
            Calendar l2Cal = Calendar.getInstance();
            Calendar l3Cal = Calendar.getInstance();
            Calendar c1 = Calendar.getInstance();
            Calendar c2 = Calendar.getInstance();
            Calendar c3 = Calendar.getInstance();
            Calendar max = Calendar.getInstance();

            Date m1Dat, m2Dat, m3Dat, f1Dat, f2Dat, f3Dat, o1Dat, o2Dat, o3Dat, l1Dat, l2Dat, l3Dat;
            if (daysBetween <= laengeMens) {
                binding.calenderviewPhasen.setMaximumDate(max);
                m1Dat = Date.from(date1.plusDays(zyklus).atStartOfDay(zoneId).toInstant());
                m2Dat = Date.from(date1.plusDays(zyklus * 2).atStartOfDay(zoneId).toInstant());
                m3Dat = Date.from(date1.plusDays(zyklus * 3).atStartOfDay(zoneId).toInstant());
                f1Dat = Date.from(date1.plusDays(laengeMens).atStartOfDay(zoneId).toInstant());
                f2Dat = Date.from(date1.plusDays(laengeMens + zyklus).atStartOfDay(zoneId).toInstant());
                f3Dat = Date.from(date1.plusDays(laengeMens + zyklus * 2).atStartOfDay(zoneId).toInstant());
                o1Dat = Date.from(date1.plusDays(zyklus / 2).atStartOfDay(zoneId).toInstant());
                o2Dat = Date.from(date1.plusDays(zyklus / 2 + zyklus).atStartOfDay(zoneId).toInstant());
                o3Dat = Date.from(date1.plusDays(zyklus / 2 + zyklus * 2).atStartOfDay(zoneId).toInstant());
                l1Dat = Date.from(date1.plusDays(zyklus / 2 + 3).atStartOfDay(zoneId).toInstant());
                l2Dat = Date.from(date1.plusDays(zyklus / 2 + 3 + zyklus).atStartOfDay(zoneId).toInstant());
                l3Dat = Date.from(date1.plusDays(zyklus / 2 + 3 + zyklus * 2).atStartOfDay(zoneId).toInstant());

                c1.setTime(m1Dat);
                c2.setTime(m1Dat);
                c3.setTime(m1Dat);
                m1Cal.setTime(m1Dat);
                m2Cal.setTime(m2Dat);
                m3Cal.setTime(m3Dat);
                f1Cal.setTime(f1Dat);
                f2Cal.setTime(f2Dat);
                f3Cal.setTime(f3Dat);
                o1Cal.setTime(o1Dat);
                o2Cal.setTime(o2Dat);
                o3Cal.setTime(o3Dat);
                l1Cal.setTime(l1Dat);
                l2Cal.setTime(l2Dat);
                l3Cal.setTime(l3Dat);

                max.setTime(l3Dat);
                max.add(Calendar.DATE, 12);

                binding.textviewPhase.setText(daysBetween + ". Tag: Menstruationsphase");
                binding.textviewTrainingsempfehlung.setText("Niedrigere Intensität (z.B. lockeres Ausdauertraining, Yoga, Stretching). Hartes Krafttraining kann schaden, weil schützendes Estradiol fehlt");
            } else if (daysBetween <= 7 + laengeMens) {
                m1Dat = Date.from(date1.plusDays(zyklus).atStartOfDay(zoneId).toInstant());
                m2Dat = Date.from(date1.plusDays(zyklus * 2).atStartOfDay(zoneId).toInstant());
                m3Dat = Date.from(date1.plusDays(zyklus * 3).atStartOfDay(zoneId).toInstant());
                f1Dat = Date.from(date1.plusDays(laengeMens + zyklus).atStartOfDay(zoneId).toInstant());
                f2Dat = Date.from(date1.plusDays(laengeMens + zyklus * 2).atStartOfDay(zoneId).toInstant());
                f3Dat = Date.from(date1.plusDays(laengeMens + zyklus * 3).atStartOfDay(zoneId).toInstant());
                o1Dat = Date.from(date1.plusDays(zyklus / 2).atStartOfDay(zoneId).toInstant());
                o2Dat = Date.from(date1.plusDays(zyklus / 2 + zyklus).atStartOfDay(zoneId).toInstant());
                o3Dat = Date.from(date1.plusDays(zyklus / 2 + zyklus * 2).atStartOfDay(zoneId).toInstant());
                l1Dat = Date.from(date1.plusDays(zyklus / 2 + 3).atStartOfDay(zoneId).toInstant());
                l2Dat = Date.from(date1.plusDays(zyklus / 2 + 3 + zyklus).atStartOfDay(zoneId).toInstant());
                l3Dat = Date.from(date1.plusDays(zyklus / 2 + 3 + zyklus + zyklus).atStartOfDay(zoneId).toInstant());

                c1.setTime(m1Dat);
                c2.setTime(m1Dat);
                c3.setTime(m1Dat);
                m1Cal.setTime(m1Dat);
                m2Cal.setTime(m2Dat);
                m3Cal.setTime(m3Dat);
                f1Cal.setTime(f1Dat);
                f2Cal.setTime(f2Dat);
                f3Cal.setTime(f3Dat);
                o1Cal.setTime(o1Dat);
                o2Cal.setTime(o2Dat);
                o3Cal.setTime(o3Dat);
                l1Cal.setTime(l1Dat);
                l2Cal.setTime(l2Dat);
                l3Cal.setTime(l3Dat);

                max.setTime(m3Dat);
                max.add(Calendar.DATE, laengeMens);

                binding.textviewPhase.setText(daysBetween + ". Tag: Follikelphase");
                binding.textviewTrainingsempfehlung.setText("Perfekter Zeitpunkt für Krafttraining: Trainingseffekt besonders hoch, da Estradiol Muskulatur stärker auf Trainingsreize reagieren lässt.");
            } else if (daysBetween <= 16) {
                m1Dat = Date.from(date1.plusDays(zyklus).atStartOfDay(zoneId).toInstant());
                m2Dat = Date.from(date1.plusDays(zyklus * 2).atStartOfDay(zoneId).toInstant());
                m3Dat = Date.from(date1.plusDays(zyklus * 3).atStartOfDay(zoneId).toInstant());
                f1Dat = Date.from(date1.plusDays(laengeMens + zyklus).atStartOfDay(zoneId).toInstant());
                f2Dat = Date.from(date1.plusDays(laengeMens + zyklus * 2).atStartOfDay(zoneId).toInstant());
                f3Dat = Date.from(date1.plusDays(laengeMens + zyklus * 3).atStartOfDay(zoneId).toInstant());
                o1Dat = Date.from(date2.plusDays(zyklus).atStartOfDay(zoneId).toInstant());
                o2Dat = Date.from(date2.plusDays(zyklus * 2).atStartOfDay(zoneId).toInstant());
                o3Dat = Date.from(date2.plusDays(zyklus * 3).atStartOfDay(zoneId).toInstant());
                l1Dat = Date.from(date1.plusDays(zyklus / 2 + 3).atStartOfDay(zoneId).toInstant());
                l2Dat = Date.from(date1.plusDays(zyklus / 2 + 3 + zyklus).atStartOfDay(zoneId).toInstant());
                l3Dat = Date.from(date1.plusDays(zyklus / 2 + 3 + zyklus * 2).atStartOfDay(zoneId).toInstant());

                c1.setTime(m1Dat);
                c2.setTime(m1Dat);
                c3.setTime(m1Dat);
                m1Cal.setTime(m1Dat);
                m2Cal.setTime(m2Dat);
                m3Cal.setTime(m3Dat);
                f1Cal.setTime(f1Dat);
                f2Cal.setTime(f2Dat);
                f3Cal.setTime(f3Dat);
                o1Cal.setTime(o1Dat);
                o2Cal.setTime(o2Dat);
                o3Cal.setTime(o3Dat);
                l1Cal.setTime(l1Dat);
                l2Cal.setTime(l2Dat);
                l3Cal.setTime(l3Dat);

                max.setTime(f3Dat);
                max.add(Calendar.DATE, (zyklus / 2 - laengeMens));

                binding.textviewPhase.setText(daysBetween + ". Tag: Ovulation");
                binding.textviewTrainingsempfehlung.setText("Auf Körper hören");
            } else if (daysBetween <= zyklus) {
                m1Dat = Date.from(date1.plusDays(zyklus).atStartOfDay(zoneId).toInstant());
                m2Dat = Date.from(date1.plusDays(zyklus * 2).atStartOfDay(zoneId).toInstant());
                m3Dat = Date.from(date1.plusDays(zyklus * 3).atStartOfDay(zoneId).toInstant());
                f1Dat = Date.from(date1.plusDays(laengeMens + zyklus).atStartOfDay(zoneId).toInstant());
                f2Dat = Date.from(date1.plusDays(laengeMens + zyklus * 2).atStartOfDay(zoneId).toInstant());
                f3Dat = Date.from(date1.plusDays(laengeMens + zyklus * 3).atStartOfDay(zoneId).toInstant());
                o1Dat = Date.from(date1.plusDays(zyklus / 2 + zyklus).atStartOfDay(zoneId).toInstant());
                o2Dat = Date.from(date1.plusDays(zyklus / 2 + zyklus * 2).atStartOfDay(zoneId).toInstant());
                o3Dat = Date.from(date1.plusDays(zyklus / 2 + zyklus * 3).atStartOfDay(zoneId).toInstant());
                l1Dat = Date.from(date1.plusDays(zyklus / 2 + 3 + zyklus).atStartOfDay(zoneId).toInstant());
                l2Dat = Date.from(date1.plusDays(zyklus / 2 + 3 + zyklus * 2).atStartOfDay(zoneId).toInstant());
                l3Dat = Date.from(date1.plusDays(zyklus / 2 + 3 + zyklus * 3).atStartOfDay(zoneId).toInstant());

                c1.setTime(m1Dat);
                c2.setTime(m1Dat);
                c3.setTime(m1Dat);
                m1Cal.setTime(m1Dat);
                m2Cal.setTime(m2Dat);
                m3Cal.setTime(m3Dat);
                f1Cal.setTime(f1Dat);
                f2Cal.setTime(f2Dat);
                f3Cal.setTime(f3Dat);
                o1Cal.setTime(o1Dat);
                o2Cal.setTime(o2Dat);
                o3Cal.setTime(o3Dat);
                l1Cal.setTime(l1Dat);
                l2Cal.setTime(l2Dat);
                l3Cal.setTime(l3Dat);

                max.setTime(l3Dat);
                max.add(Calendar.DATE, zyklus / 2);

                binding.textviewPhase.setText(daysBetween + ". Tag: Lutealphase");
                binding.textviewTrainingsempfehlung.setText("Trainingsintensität und -volumen reduzieren: leichteres Krafttraining, lockere Regenerationsläufe");
            }

            binding.calenderviewPhasen.setMaximumDate(max);

            List<EventDay> phasen = new ArrayList<>();

            phasen.add(new EventDay(m1Cal, R.drawable.ic_mens));
            phasen.add(new EventDay(m2Cal, R.drawable.ic_mens));
            phasen.add(new EventDay(m3Cal, R.drawable.ic_mens));

            phasen.add(new EventDay(f1Cal, R.drawable.ic_follikel));
            phasen.add(new EventDay(f2Cal, R.drawable.ic_follikel));
            phasen.add(new EventDay(f3Cal, R.drawable.ic_follikel));

            phasen.add(new EventDay(o1Cal, R.drawable.ic_ovulation));
            phasen.add(new EventDay(o2Cal, R.drawable.ic_ovulation));
            phasen.add(new EventDay(o3Cal, R.drawable.ic_ovulation));

            phasen.add(new EventDay(l1Cal, R.drawable.ic_luteal, Color.parseColor("#66d9ff")));
            phasen.add(new EventDay(l2Cal, R.drawable.ic_luteal, Color.parseColor("#66d9ff")));
            phasen.add(new EventDay(l3Cal, R.drawable.ic_luteal, Color.parseColor("#66d9ff")));

            binding.calenderviewPhasen.setEvents(phasen);
        } else {
            binding.textviewPhase.setText((daysBetween - zyklus) + " Tage zu spät");
            binding.textviewTrainingsempfehlung.setText("Keine Vorhersage möglich");
        }
    }
}