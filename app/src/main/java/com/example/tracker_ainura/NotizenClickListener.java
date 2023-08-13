package com.example.tracker_ainura;

import androidx.cardview.widget.CardView;

import com.example.tracker_ainura.Models.Notizen;

public interface NotizenClickListener {
    void onClick(Notizen notiz);
    void onLongClick(Notizen notiz, CardView cardview);
}
