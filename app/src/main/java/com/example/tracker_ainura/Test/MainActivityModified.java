package com.example.tracker_ainura.Test;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;

/**
 * Genau wie MainActivity, aber ohne Android-Logik. Von AusrechnenTester.java getestet
 * (Ich habe den Fehler gemacht, dass ich die Tests erst am Ende entwickelt habe und dann auf die schon existierenden Klassen anpassen musste. Ich weiss, Sie haben es mir sogar gesagt, aber ich habe mich vor den automatischen Tests gedrückt.)
 * */
public class MainActivityModified {
    public String nurAusrechnen(LocalDate date1, int laengeMens, int zyklus) {//sehr hässliche und lange Methode, aber ich habe keinen Weg gefunden, um sie zu kürzen (Calendars und Dates vertragen sich nicht mit Schleifen, was eine sehr elegante Lösung hätte sein können)

        LocalDate date2 = LocalDate.now();
        long daysBetween = ChronoUnit.DAYS.between(date1, date2.plusDays(1));
        ZoneId zoneId = ZoneId.systemDefault();

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

        // die Calendars werden durch die Dates eingestellt
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
            m0Dat = Date.from(date1.plusDays(0).atStartOfDay(zoneId).toInstant());
            m0Cal.setTime(m0Dat);
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
        } else if (daysBetween <= 16) {
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
        }
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

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

        // Format the Calendar's date
        String m = dateFormat.format(m1Cal.getTime());
        String f = dateFormat.format(f1Cal.getTime());
        String o = dateFormat.format(o1Cal.getTime());
        String l = dateFormat.format(l1Cal.getTime());

        String datesForTest = m + ", " + f + ", " + o + ", " + l;
        return datesForTest;
    }
}