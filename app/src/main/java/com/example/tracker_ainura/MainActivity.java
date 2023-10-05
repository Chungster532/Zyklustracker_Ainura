package com.example.tracker_ainura;

import android.app.AlarmManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

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
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    RoomDB database_zyklen;
    ZyklenListeAdapter zyklenListeAdapter;
    List<Zyklen> zyklen = new ArrayList<>();
    private AlarmManager alarmManager;

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
        setUpInfo();

        createWorkRequest();
    }

    private void createWorkRequest() {
        PeriodicWorkRequest dailyWorkRequest = new PeriodicWorkRequest.Builder(ReminderWorker.class, 1, TimeUnit.MINUTES).build();
        WorkManager.getInstance().enqueue(dailyWorkRequest);
    }

    private void setUpInfo() {
        boolean clicked = false;
        binding.cardviewInfo.setVisibility(View.INVISIBLE);
        binding.btnInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                info(clicked);
            }
        });
    }

    private void info(boolean clicked) {
        clicked = !clicked;
        if (clicked) {
            binding.cardviewInfo.setVisibility(View.VISIBLE);
        } else {
            binding.cardviewInfo.setVisibility(View.INVISIBLE);
        }
        binding.cardviewInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.cardviewInfo.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void setUpBtn() {
        SharedPreferences prefs = getSharedPreferences("SharedPrefs", MODE_PRIVATE);
        boolean periode = prefs.getBoolean("Periode", false);
        if (periode) {
            binding.buttonErsterTag.setText("Ende der Periode?");
            binding.buttonErsterTag.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent endePeriodeIntent = new Intent(MainActivity.this, EndePeriodeActivity.class);
                    startActivity(endePeriodeIntent);
                }
            });
        } else {
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

    private void ausrechnen(LocalDate date1, int laengeMens, int zyklus) {//sehr hässliche und lange Methode, aber ich habe keinen Weg gefunden, um sie zu kürzen (die Event-Liste scheint ihre Eigenheiten zu haben)

        Calendar min = Calendar.getInstance();
        min.add(Calendar.DATE, -1);
        binding.calenderviewPhasen.setMinimumDate(min);

        LocalDate date2 = LocalDate.now();
        long daysBetween = ChronoUnit.DAYS.between(date1, date2.plusDays(1));
        ZoneId zoneId = ZoneId.systemDefault();

        if (daysBetween <= zyklus) {
            List<EventDay> phasen = new ArrayList<>();
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
            Calendar max = Calendar.getInstance();

            Date m1Dat = new Date();
            Date m2Dat = new Date();
            Date m3Dat = new Date();
            Date f1Dat = new Date();
            Date f2Dat = new Date();
            Date f3Dat = new Date();
            Date o1Dat = new Date();
            Date o2Dat = new Date();
            Date o3Dat = new Date();
            Date l1Dat = new Date();
            Date l2Dat = new Date();
            Date l3Dat = new Date();
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

                max.setTime(l3Dat);
                max.add(Calendar.DATE, zyklus / 2);

                binding.textviewPhase.setText(daysBetween + ". Tag: Lutealphase");
                binding.textviewTrainingsempfehlung.setText("Trainingsintensität und -volumen reduzieren: leichteres Krafttraining, lockere Regenerationsläufe");
            }

            binding.calenderviewPhasen.setMaximumDate(max);

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

            setUpIcons(phasen, m1Cal, m2Cal, m3Cal, f1Cal, f2Cal, f3Cal, o1Cal, o2Cal, o3Cal, l1Cal, l2Cal, l3Cal, laengeMens);

            binding.calenderviewPhasen.setEvents(phasen);
        } else {
            binding.textviewPhase.setText((daysBetween - zyklus) + " Tage zu spät");
            binding.textviewTrainingsempfehlung.setText("Keine Vorhersage möglich");
        }
    }

    private void setUpIcons(List<EventDay> phasen, Calendar m1Cal, Calendar m2Cal, Calendar m3Cal, Calendar f1Cal, Calendar f2Cal, Calendar f3Cal, Calendar o1Cal, Calendar o2Cal, Calendar o3Cal, Calendar l1Cal, Calendar l2Cal, Calendar l3Cal, int laengeMens) {
        phasen.add(new EventDay(m1Cal, R.drawable.ic_mens));
        phasen.add(new EventDay(m2Cal, R.drawable.ic_mens));
        phasen.add(new EventDay(m3Cal, R.drawable.ic_mens));

        phasen.add(new EventDay(f1Cal, R.drawable.ic_follikel));
        phasen.add(new EventDay(f2Cal, R.drawable.ic_follikel));
        phasen.add(new EventDay(f3Cal, R.drawable.ic_follikel));

        phasen.add(new EventDay(o1Cal, R.drawable.ic_ovulation));
        phasen.add(new EventDay(o2Cal, R.drawable.ic_ovulation));
        phasen.add(new EventDay(o3Cal, R.drawable.ic_ovulation));

        phasen.add(new EventDay(l1Cal, R.drawable.ic_luteal));
        phasen.add(new EventDay(l2Cal, R.drawable.ic_luteal));
        phasen.add(new EventDay(l3Cal, R.drawable.ic_luteal));
    }
}