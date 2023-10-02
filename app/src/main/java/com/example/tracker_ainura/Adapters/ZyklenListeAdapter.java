package com.example.tracker_ainura.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tracker_ainura.Models.Zyklen;
import com.example.tracker_ainura.NotizenClickListener;
import com.example.tracker_ainura.R;
import com.example.tracker_ainura.ZyklenClickListener;

import java.util.List;

public class ZyklenListeAdapter extends RecyclerView.Adapter<ZyklenViewHolder> {

    Context context;
    List<Zyklen> liste;
    ZyklenClickListener listener;

    public ZyklenListeAdapter(Context context, List<Zyklen> liste){
        this.context = context;
        this.liste = liste;
    }

    @NonNull
    @Override
    public ZyklenViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ZyklenViewHolder(LayoutInflater.from(context).inflate(R.layout.zyklen_liste, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ZyklenViewHolder holder, int position) {
        holder.textview_beschreibung.setText(liste.get(position).getStart()+" - "+liste.get(position).getEnde()+" ("+liste.get(position).getLaenge()+" Tage), Periode: "+liste.get(position).getLaengePeriode()+" Tage");

        holder.cardview_zyklencontainer.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                listener.onLongClick(liste.get(holder.getAdapterPosition()), holder.cardview_zyklencontainer);
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return liste.size();
    }
}

class ZyklenViewHolder extends RecyclerView.ViewHolder{
    CardView cardview_zyklencontainer;
    TextView textview_beschreibung;

    public ZyklenViewHolder(@NonNull View itemView) {
        super(itemView);
        cardview_zyklencontainer = itemView.findViewById(R.id.cardview_zyklencontainer);
        textview_beschreibung = itemView.findViewById(R.id.textview_beschreibung);
    }
}
