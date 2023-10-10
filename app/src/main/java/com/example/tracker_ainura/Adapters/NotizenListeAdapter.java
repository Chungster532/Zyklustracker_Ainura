package com.example.tracker_ainura.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tracker_ainura.Models.Notizen;
import com.example.tracker_ainura.NotizenClickListener;
import com.example.tracker_ainura.R;

import java.util.List;

/**
 * Adapter in TagebuchActivity
 * */
public class NotizenListeAdapter extends RecyclerView.Adapter<NotizenViewHolder>{

    Context context;
    List<Notizen> liste;
    NotizenClickListener listener;
    public NotizenListeAdapter(Context context, List<Notizen> liste, NotizenClickListener listener) {
        this.context = context;
        this.liste = liste;
        this.listener = listener;
    }

    @NonNull
    @Override
    public NotizenViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NotizenViewHolder(LayoutInflater.from(context).inflate(R.layout.notizen_liste, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NotizenViewHolder holder, int position) {
        holder.textview_datum.setText(liste.get(position).getDatum());
        holder.textview_tagZyklus.setText(liste.get(position).getTagZyklus()+". Tag");
        holder.textview_training.setText("Training: "+liste.get(position).getTraining());
        holder.textview_blutung.setText("Blutung: "+liste.get(position).getBlutung());
        holder.textview_stimmung.setText("Stimmung: "+liste.get(position).getStimmung());
        holder.textview_sonstiges.setText("Sonstiges: "+liste.get(position).getSonstiges());

        holder.cardview_notizencontainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClick(liste.get(holder.getAdapterPosition()));
            }
        });

        holder.cardview_notizencontainer.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                listener.onLongClick(liste.get(holder.getAdapterPosition()), holder.cardview_notizencontainer);
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return liste.size();
    }

    public void filterList(List<Notizen> filteredList){
        liste = filteredList;
        notifyDataSetChanged();
    }
}

class NotizenViewHolder extends RecyclerView.ViewHolder{

    CardView cardview_notizencontainer;
    TextView textview_datum, textview_tagZyklus, textview_training, textview_blutung, textview_stimmung, textview_sonstiges;
    public NotizenViewHolder(@NonNull View itemView) {
        super(itemView);
        cardview_notizencontainer = itemView.findViewById(R.id.cardview_notizencontainer);
        textview_datum = itemView.findViewById(R.id.textview_datum);
        textview_tagZyklus = itemView.findViewById(R.id.textview_tagZyklus);
        textview_training = itemView.findViewById(R.id.textview_training);
        textview_blutung = itemView.findViewById(R.id.textview_blutung);
        textview_stimmung = itemView.findViewById(R.id.textview_stimmung);
        textview_sonstiges = itemView.findViewById(R.id.textview_sonstiges);
    }
}