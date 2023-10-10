package com.example.tracker_ainura;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Hilfsklasse, formatiert Strings aus Datepicker.
 * -> reduziert Code
 * */
public class DateRetriever {
    public String convertDateFromDatePicker(int tag, String jahr, int monat){
        String monatStrFertig;
        String tagStrFertig;
        monat += 1;
        if(tag<10){
            String tagStr = Integer.toString(tag);
            tagStrFertig = "0"+tagStr;
        }else{
            tagStrFertig = Integer.toString(tag);
        }
        if(monat<10){
            String monatStr = Integer.toString(monat);
            monatStrFertig = "0"+monatStr;
        }else{
            monatStrFertig = Integer.toString(monat);
        }

        String letztePeriode = tagStrFertig+"-"+monatStrFertig+"-"+jahr;
        return letztePeriode;
    }
}
