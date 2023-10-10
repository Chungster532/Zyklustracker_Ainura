package com.example.tracker_ainura;

import androidx.cardview.widget.CardView;

import com.example.tracker_ainura.Models.Notizen;

/**
 * Interface für Interaktionen mit Notizen auf TagebuchActivity
 * */
public interface NotizenClickListener {
    void onClick(Notizen notiz);
    void onLongClick(Notizen notiz, CardView cardview);
}
