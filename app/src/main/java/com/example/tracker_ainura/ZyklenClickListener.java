package com.example.tracker_ainura;

import androidx.cardview.widget.CardView;

import com.example.tracker_ainura.Models.Notizen;
import com.example.tracker_ainura.Models.Zyklen;

public interface ZyklenClickListener {
    void onLongClick(Zyklen zyklus, CardView cardview);
}
