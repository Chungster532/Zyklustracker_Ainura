package com.example.tracker_ainura;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.tracker_ainura.Adapters.WissenThemaAdapter;
import com.example.tracker_ainura.Models.WissenThema;
import com.example.tracker_ainura.databinding.ActivityEinstellungenBinding;
import com.example.tracker_ainura.databinding.ActivityWissenBinding;

import java.util.ArrayList;
import java.util.List;

public class WissenActivity extends AppCompatActivity {

    private ActivityWissenBinding binding;
    List<WissenThema> wissenThemaListe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWissenBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        initialisiereListe();
        updateRecyclerview();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.item_einstellungen:
                Intent einstellungenIntent = new Intent(WissenActivity.this, EinstellungenActivity.class);
                startActivity(einstellungenIntent);
                return true;
            case R.id.item_zusammenfassung:
                Intent zusammenfassungIntent = new Intent(WissenActivity.this, ZusammenfassungActivity.class);
                zusammenfassungIntent.putExtra("ausNeuerPeriode", false);
                startActivity(zusammenfassungIntent);
                return true;
            case R.id.item_tagebuch_notiz:
                Intent notizenIntent = new Intent(WissenActivity.this, TagebuchActivity.class);
                startActivity(notizenIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateRecyclerview() {
        binding.recyclerviewWissen.setHasFixedSize(true);
        binding.recyclerviewWissen.setLayoutManager(new LinearLayoutManager(this));
        WissenThemaAdapter wissenThemaAdapter = new WissenThemaAdapter(WissenActivity.this, wissenThemaListe);
        binding.recyclerviewWissen.setAdapter(wissenThemaAdapter);
    }

    private void initialisiereListe() {
        wissenThemaListe = new ArrayList<>();
        wissenThemaListe.add(new WissenThema("Zyklusbasiertes Training", "Beim zyklusbasierten Training geht es darum, die für die jeweiligen Phasen charakteristische Hormonkonzentrationen auszunutzen. Wichtig zu beachten ist jedoch, dass der Menstruationszyklus von Frau zu Frau stark variieren kann und ein Trainingsplan immer individuell erarbeitet werden muss."));
        wissenThemaListe.add(new WissenThema("Menstruationsphase", "1. bis 3. bzw. 7. Tag im Zyklus (Dauer: 3-7 Tage)\n" + "Vorgänge: Abstossung Gebärmutterschleimhaut verursacht Blutung\nHormone: Einstellung der Progesteron-Produktion\nFolgen: Regelschmerzen, Erschöpfung, Kraftlosigkeit\nTrainingsempfehlung: Niedrigere Intensität (z.B. lockeres Ausdauertraining, Yoga, Stretching). Hartes Krafttraining kann schaden, weil schützendes Estradiol fehlt"));
        wissenThemaListe.add(new WissenThema("Follikelphase", "4. bzw. 8. bis 11. bzw. 18. Tag im Zyklus (Dauer: 7-10 Tage)\nVorgänge: Vorbereitung auf nächsten Eisprung\nHormone: Erhöhte Produktion von FSH, verursacht erhöhte Produktion von Östrogen, Estradiolspiegel steigt\nFolgen: Steigender Estradiolspiegel erhöht Energielevel\nTrainingsempfehlung: Perfekter Zeitpunkt für Krafttraining: Trainingseffekt besonders hoch, da Estradiol Muskulatur stärker auf Trainingsreize reagieren lässt."));
        wissenThemaListe.add(new WissenThema("Ovulation", "12. bzw. 19. bis 13. bzw. 18. Tag im Zyklus (Dauer: 3-5 Tage)\nVorgänge: Eizelle wird aus Eistock in Eileiter gestossen\nHormone: Estradiolspiegel sinkt, Progesteronspiegel steigt\nFolgen: Regelschmerzen, Erschöpfung, Kraftlosigkeit\nTrainingsempfehlung: Auf Körper hören"));
        wissenThemaListe.add(new WissenThema("Lutealphase", "14. bzw. 19. bis 26. bzw. 28 Tag im Zyklus (Dauer: 10-14 Tage)\nVorgänge: Gebärmutterschleimhaut wird wieder aufgebaut\nHormone: Progesteronspiegel steigt deutlich, Östrogenspiegel steigt leicht an.\nFolgen: Energielevel, Reaktionsgeschwindigkeit, Koordination und Feinmotorik verschlechtern sich infolge der Progesteronproduktion\nTrainingsempfehlung: Trainingsintensität und -volumen reduzieren: leichteres Krafttraining, lockere Regenerationsläufe"));
    }
}