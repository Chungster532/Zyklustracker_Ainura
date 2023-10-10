package com.example.tracker_ainura;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * Klasse, die alle 24h kontrolliert, ob morgen Mensphase beginnt. Falls ja -> Benachrichtigung
 * */
public class ReminderWorker extends Worker {
    public ReminderWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        SharedPreferences prefs = getApplicationContext().getSharedPreferences("SharedPrefs", Context.MODE_PRIVATE);
        String strLaenge = prefs.getString("laenge", "");
        int laenge = Integer.parseInt(strLaenge);
        String letztePeriode = prefs.getString("letztePeriode", "");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate date1 = LocalDate.parse(letztePeriode, formatter);
        LocalDate date2 = LocalDate.now();
        long daysBetween = ChronoUnit.DAYS.between(date1, date2.plusDays(1));
        int tag = (int) daysBetween;
        if (tag==(laenge-1)){
            sendNotfication(tag);
        }
        return Result.success();
    }

    @SuppressLint("MissingPermission")
    private void sendNotfication(int tag) {
        Intent openApp = new Intent(getApplicationContext(), MainActivity.class);
        openApp.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, openApp, 0);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext(), "ReminderChannel")
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle("Menstruationsphase fängt bald an")
                .setContentText("Heute ist der "+tag+". Tag deines Menstruationszyklus")
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
        notificationManagerCompat.notify(1, notificationBuilder.build());
    }
}
