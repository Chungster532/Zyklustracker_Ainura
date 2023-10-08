package com.example.tracker_ainura.Test;

import java.time.LocalDate;

/**
 * Testet Richtigkeit der ausgerechneten Daten (müssen manuell kontrolliert werden)
 * Diese Klasse läuft nicht in Android-Studio und muss deshalb zusammen mit MainActivityModified in einer anderen IDE verwendet werden
 * */
public class AusrechnenTester {
    public static void main(String[] args) {
        MainActivityModified mainActivity = new MainActivityModified();
        int p = 4;
        int z = 35;
        LocalDate date1 = LocalDate.of(2023, 12, 18);
        System.out.println(mainActivity.nurAusrechnen(date1, p, z));
    }
}
