package com.example.tracker_ainura.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tracker_ainura.Models.WissenThema;
import com.example.tracker_ainura.R;

import java.util.List;

/**
 * Adapter für Infos der Wissens-Activity
 * */
public class WissenThemaAdapter extends RecyclerView.Adapter<WissenThemaViewHolder> {

    List<WissenThema> liste;
    Context context;

    public WissenThemaAdapter(Context context, List<WissenThema> liste) {
        this.context = context;
        this.liste = liste;
    }

    @NonNull
    @Override
    public WissenThemaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new WissenThemaViewHolder(LayoutInflater.from(context).inflate(R.layout.wissen_liste, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull WissenThemaViewHolder holder, int position) {
        holder.textview_titel.setText(liste.get(position).getThema());
        holder.textview_infos.setText(liste.get(position).getInfos());

        boolean istAusgeklappt = liste.get(position).isIstAusgeklappt();
        holder.textview_infos.setVisibility(istAusgeklappt? View.VISIBLE:View.GONE);

        holder.textview_titel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WissenThema wissenThema = liste.get(holder.getAdapterPosition());
                wissenThema.setIstAusgeklappt(!wissenThema.isIstAusgeklappt());
                notifyItemChanged(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return liste.size();
    }
}

class WissenThemaViewHolder extends RecyclerView.ViewHolder{

    CardView cardview_wissencontainer;
    TextView textview_titel, textview_infos;

    public WissenThemaViewHolder(@NonNull View itemView) {
        super(itemView);
        cardview_wissencontainer = itemView.findViewById(R.id.cardview_wissencontainer);
        textview_titel = itemView.findViewById(R.id.textview_titel);
        textview_infos = itemView.findViewById(R.id.textview_infos);
    }
}
