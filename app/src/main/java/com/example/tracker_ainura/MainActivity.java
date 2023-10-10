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
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
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

/**
 * Start-Activity:
 *
 * rechnet aus und präsentiert nächste Phasen mit Trainingsempfehlung.
 * von hier aus wird ein Zyklus gestartet/beendet.
 * */
public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    RoomDB database_zyklen;
    ZyklenListeAdapter zyklenListeAdapter;
    List<Zyklen> zyklen = new ArrayList<>();
    private AlarmManager alarmManager;
    List<EventDay> phasen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        database_zyklen = RoomDB.getInstance(this);
        zyklen = database_zyklen.zyklenDao().getAll();

        // Daten aus SharedPreferences holen und umformen
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
        setUpAktuell();

        createWorkRequest();
    }

    /**
     * Methode, die Clicklistener für Info-Icon auf Aktuell-CardView aufsetzt (führt zu Wissen-Activity)
     * */
    private void setUpAktuell() {
        binding.btnInfoWissen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent wissenIntent = new Intent(MainActivity.this, WissenActivity.class);
                startActivity(wissenIntent);
            }
        });
    }

    /**
     * Methode, welche Benachrichtigung auslöst (bzw. sollte, funktioniert nicht, obwohl keine Fehler gemeldet werden. Problem liegt wahrscheinlich in den Berechtigungen.
     **/
    private void createWorkRequest() {
        PeriodicWorkRequest dailyWorkRequest = new PeriodicWorkRequest.Builder(ReminderWorker.class, 1, TimeUnit.MINUTES).build();
        WorkManager.getInstance().enqueue(dailyWorkRequest);
    }

    /**
     * Methode, die Legende zu Phasen-Icons zeigt, wenn Info-Icon gedrückt wird. (ruft info(boolean) auf)
     **/
    private void setUpInfo() {
        boolean clicked = false;
        binding.cardviewInfo.setVisibility(View.INVISIBLE);
        binding.btnInfoMehr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                info(clicked);
            }
        });
    }

    /**
     * Methode, die kontrolliert, die sich um Sichtbarkeit der Info kümmert. Musste von setUpInfo() getrennt werden, da innere Klasse finale Variable will, aber boolean clicked nicht final sein darf.
     **/
    private void info(boolean clicked) {
        clicked = !clicked;
        if (clicked) {
            binding.cardviewInfo.setVisibility(View.VISIBLE);
        } else {
            binding.cardviewInfo.setVisibility(View.INVISIBLE);
        }
        binding.btnInfoWeg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.cardviewInfo.setVisibility(View.INVISIBLE);
            }
        });
    }

    /**
     * Methode, die sich um Zyklus/Blutung-Btn kümmert.
     * Blutung-Zustand wird aus SharedPreferences geholt -> bestimmt Text und Intent des Btns (führt entweder zu NeuePeriodeActivity oder EndePeriodeActivity)
     * */
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

    /**
     * Methode, die Anfangstage der Phasen der nächsten 3 Zyklen ausrechnet (Rechnungen werden weiter unten erklärt).
     * Ruft setUpIcons() auf, die dann CalendarView gestaltet
     *
     * @param date1 der erste Tag des aktuellen Zyklus
     * @param laengeMens Länge der Menstruationsphase in Tagen
     * @param zyklus Länge des gesamten Zyklus in Tagen*/
    public void ausrechnen(LocalDate date1, int laengeMens, int zyklus) {//sehr hässliche und lange Methode, aber ich habe keinen Weg gefunden, um sie zu kürzen (Calendars und Dates vertragen sich nicht mit Schleifen, was eine sehr elegante Lösung hätte sein können)

        Calendar min = Calendar.getInstance();
        min.add(Calendar.DATE, -1);
        binding.calenderviewPhasen.setMinimumDate(min);

        LocalDate date2 = LocalDate.now();
        long daysBetween = ChronoUnit.DAYS.between(date1, date2.plusDays(1));
        ZoneId zoneId = ZoneId.systemDefault();

        // Calendars werden gebraucht, um EventDays zu erstellen. Eine Liste mit Eventdays regelt die Darstellung der Icons im CalendarView
        Calendar m0Cal = Calendar.getInstance(); // Mensphase des aktuellen Zyklus (wird nur während Mensphase angezeigt)
        Calendar m1Cal = Calendar.getInstance(); // Mensphase des nächsten Zyklus'
        Calendar m2Cal = Calendar.getInstance(); // Mensphase des übernächsten Zyklus'
        Calendar m3Cal = Calendar.getInstance(); // etc.
        Calendar f1Cal = Calendar.getInstance(); // Follikelphase des 1. Zyklus'
        Calendar f2Cal = Calendar.getInstance();
        Calendar f3Cal = Calendar.getInstance();
        Calendar o1Cal = Calendar.getInstance(); // Ovulationsphase #1
        Calendar o2Cal = Calendar.getInstance();
        Calendar o3Cal = Calendar.getInstance();
        Calendar l1Cal = Calendar.getInstance(); // Lutealphase #1
        Calendar l2Cal = Calendar.getInstance();
        Calendar l3Cal = Calendar.getInstance();
        Calendar max = Calendar.getInstance(); // wird das späteste anzeigbare Datum im CalendarView festlegen

        if (daysBetween <= zyklus) {
            List<EventDay> phasen = new ArrayList<>(); // EventDay besteht aus einem Calendar

            // die Calendars werden durch die Dates eingestellt (einfacher mit LocalDates zu manipulieren als Calendars)
            Date m0Dat = new Date();
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
                binding.textviewPhase.setText(daysBetween + ". Tag: Menstruationsphase");
                binding.textviewTrainingsempfehlung.setText("Niedrigere Intensität (z.B. lockeres Ausdauertraining, Yoga, Stretching). Hartes Krafttraining kann schaden, weil Östrogenspiegel niedrig ist");
                binding.linearlayoutAktuell.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.gradient_mens));

                m0Dat = Date.from(date1.plusDays(0).atStartOfDay(zoneId).toInstant());
                m0Cal.setTime(m0Dat);
                phasen.add(new EventDay(m0Cal, R.drawable.ic_mens));
                m1Dat = Date.from(date1.plusDays(zyklus).atStartOfDay(zoneId).toInstant()); // Mensphase = Anfang letzte Periode +
                m2Dat = Date.from(date1.plusDays(zyklus * 2).atStartOfDay(zoneId).toInstant()); // Zyklen #2 und #3 werden ausgerechnet, indem die Länge des Zyklus passend multipliziert und zum 1. Datum addiert wird
                m3Dat = Date.from(date1.plusDays(zyklus * 3).atStartOfDay(zoneId).toInstant());
                f1Dat = Date.from(date1.plusDays(laengeMens).atStartOfDay(zoneId).toInstant()); // Follikelphase folgt auf Mensphase -> Länge der Mensphase zum Beginn des letzten Zyklus addieren
                f2Dat = Date.from(date1.plusDays(laengeMens + zyklus).atStartOfDay(zoneId).toInstant());
                f3Dat = Date.from(date1.plusDays(laengeMens + zyklus * 2).atStartOfDay(zoneId).toInstant());
                o1Dat = Date.from(date1.plusDays(zyklus / 2).atStartOfDay(zoneId).toInstant()); // Ovulation ist meistens 12-14 Tage vor Ende oder in der Mitte des Zyklus'(Berechnung trifft nur für 24-28 Zyklen zu)
                o2Dat = Date.from(date1.plusDays(zyklus / 2 + zyklus).atStartOfDay(zoneId).toInstant());
                o3Dat = Date.from(date1.plusDays(zyklus / 2 + zyklus * 2).atStartOfDay(zoneId).toInstant());
                l1Dat = Date.from(date1.plusDays(zyklus / 2 + 3).atStartOfDay(zoneId).toInstant()); // Der sportliche Effekt der Ovulationsphase hält 3 Tage an, danach folgt Lutealphase
                l2Dat = Date.from(date1.plusDays(zyklus / 2 + 3 + zyklus).atStartOfDay(zoneId).toInstant());
                l3Dat = Date.from(date1.plusDays(zyklus / 2 + 3 + zyklus * 2).atStartOfDay(zoneId).toInstant());

                max.setTime(l3Dat);
                max.add(Calendar.DATE, 12);
            } else if (daysBetween <= 7 + laengeMens) {
                binding.linearlayoutAktuell.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.gradient_follikel));
                binding.textviewPhase.setText(daysBetween + ". Tag: Follikelphase");
                binding.textviewTrainingsempfehlung.setText("Perfekter Zeitpunkt für Krafttraining: Trainingseffekt besonders hoch, da Östrogene Muskulatur stärker auf Trainingsreize reagieren lassen und Erholung fördern. Achtung: erhöhte Dehnbarkeit Sehnen und somit Verletzungsanfälligkeit");

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
            } else if (daysBetween <= 16) {
                binding.textviewPhase.setText(daysBetween + ". Tag: Ovulation");
                binding.textviewTrainingsempfehlung.setText("Auf Körper hören (viele fühlen sich nun am leistungsstärksten, manche hingegen erschöpft). Progesteron führt zu höherer Körpertemperatur und Puls, daher evtl. Training je nach Temperatur anpassen.");
                binding.linearlayoutAktuell.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.gradient_ovulation));

                m1Dat = Date.from(date1.plusDays(zyklus).atStartOfDay(zoneId).toInstant());
                m2Dat = Date.from(date1.plusDays(zyklus * 2).atStartOfDay(zoneId).toInstant());
                m3Dat = Date.from(date1.plusDays(zyklus * 3).atStartOfDay(zoneId).toInstant());
                f1Dat = Date.from(date1.plusDays(laengeMens + zyklus).atStartOfDay(zoneId).toInstant());
                f2Dat = Date.from(date1.plusDays(laengeMens + zyklus * 2).atStartOfDay(zoneId).toInstant());
                f3Dat = Date.from(date1.plusDays(laengeMens + zyklus * 3).atStartOfDay(zoneId).toInstant());
                o1Dat = Date.from(date1.plusDays(zyklus/2).atStartOfDay(zoneId).toInstant());
                o2Dat = Date.from(date1.plusDays(zyklus/2+zyklus).atStartOfDay(zoneId).toInstant());
                o3Dat = Date.from(date1.plusDays(zyklus/2+zyklus*2).atStartOfDay(zoneId).toInstant());
                l1Dat = Date.from(date1.plusDays(zyklus / 2 + 3).atStartOfDay(zoneId).toInstant());
                l2Dat = Date.from(date1.plusDays(zyklus / 2 + 3 + zyklus).atStartOfDay(zoneId).toInstant());
                l3Dat = Date.from(date1.plusDays(zyklus / 2 + 3 + zyklus * 2).atStartOfDay(zoneId).toInstant());

                max.setTime(f3Dat);
                max.add(Calendar.DATE, (zyklus / 2 - laengeMens));
            } else if (daysBetween <= zyklus) {
                binding.textviewPhase.setText(daysBetween + ". Tag: Lutealphase");
                binding.textviewTrainingsempfehlung.setText("Trainingsintensität und -volumen reduzieren: leichteres Krafttraining, lockere Regenerationsläufe. Auf erhaltendes, nicht unbedingt aufbauendes Training setzen.");
                binding.linearlayoutAktuell.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.gradient_luteal));

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
        } else {
            binding.textviewPhase.setText((daysBetween - zyklus) + " Tage zu spät");
            binding.textviewTrainingsempfehlung.setText("Keine Vorhersage möglich");
        }
    }

    /**
     * Methode, die EventDay-List ausfüllt
     * Parameter: vorher erstellte Calendars
     * */
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

        binding.calenderviewPhasen.setEvents(phasen);
    }
}