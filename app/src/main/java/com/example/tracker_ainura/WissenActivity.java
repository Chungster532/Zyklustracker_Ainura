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

/**
 * Wissens-Activity:
 *
 * stellt Informationen als CardViews in einem RecyclerView dar
 * */
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

    /**
     * Methode, die Layoutmanager und Liste dem RecyclerView gibt
     * */
    private void updateRecyclerview() {
        binding.recyclerviewWissen.setHasFixedSize(true);
        binding.recyclerviewWissen.setLayoutManager(new LinearLayoutManager(this));
        WissenThemaAdapter wissenThemaAdapter = new WissenThemaAdapter(WissenActivity.this, wissenThemaListe);
        binding.recyclerviewWissen.setAdapter(wissenThemaAdapter);
    }

    /**
     * Methode, die Liste mit den Themen für RecyclerView macht
     * */
    private void initialisiereListe() {
        wissenThemaListe = new ArrayList<>();
        wissenThemaListe.add(new WissenThema("Zyklusbasiertes Training", "Beim zyklusbasierten Training geht es darum, die für die jeweiligen Phasen charakteristische Hormonkonzentrationen auszunutzen. Wichtig zu beachten ist jedoch, dass der Menstruationszyklus von Frau zu Frau stark variieren kann und ein Trainingsplan immer individuell erarbeitet werden muss. Zudem geht es beim zyklusbasierten Training nicht nur um die Trainingsoptimierung. Den eigenen Zyklus und die dadurch entstehenden Phänomene als solche zu verstehen kann entlastend wirken."));
        wissenThemaListe.add(new WissenThema("Tipps für Tagebuch", "Um zu verstehen, wann dein Körper wie auf Trainingsreize reagiert, kannst du folgende Informationen dokumentieren:\n-Menstruationsblutung (Beginn, Dauer, Stärke)\n-Stimmung\n-Trainingseinheit und Gefühl während Training\n-Schmerzen, Krankheit, Verletzungen\n-Schlafqualität\n-Leistungsorientierte Sportlerinnen: Basaltemperatur\nAuf dieser Basis kann überlegt werden, wie das Training angepasst werden kann."));
        wissenThemaListe.add(new WissenThema("1. Phase: Menstruationsphase", "Dauer: 3-7 Tage\n" + "Vorgänge: Abstossung Gebärmutterschleimhaut verursacht Blutung\nHormone: Tiefster Östrogen- und Progesteronspiegel im ganzen Zyklus\nFolgen: Regelschmerzen, Erschöpfung, Kraftlosigkeit\nTrainingsempfehlung: Niedrigere Intensität (z.B. lockeres Ausdauertraining, Yoga, Stretching). Hartes Krafttraining kann schaden, weil Östrogenspiegel niedrig ist"));
        wissenThemaListe.add(new WissenThema("2. Phase: Follikelphase", "Dauer: 7-10 Tage\nVorgänge: Vorbereitung auf nächsten Eisprung\nHormone: Stetiger Anstieg des Östrogen- und Estradiolspiegels, Progesteronspiegel immer noch tief\nFolgen: Steigender Östrogenspiegel erhöht Energielevel\nTrainingsempfehlung: Perfekter Zeitpunkt für Krafttraining: Trainingseffekt besonders hoch, da Estradiol und Östrogen Muskulatur stärker auf Trainingsreize reagieren lässt und Erholung fördert. Gleichzeitig ist aber auch Vorsichtig geboten, da Östrogene Dehnbarkeit der Sehnen und somit Verletzungsanfälligkeit erhöhen"));
        wissenThemaListe.add(new WissenThema("3. Phase: Ovulation", "Dauer: 3-5 Tage\nVorgänge: Eizelle wird aus Eistock in Eileiter gestossen\nHormone: Östrogenspiegel sinkt, Progesteronspiegel steigt\nFolgen: Energielevel sinkt, Schmerzen wegen Ovulation möglich\nTrainingsempfehlung: Auf Körper hören (die meisten Frauen fühlen sich nun am leistungsstärksten, manche hingegen erschöpft). Progesteron führt zu höherer Körpertemperatur und Puls, daher evtl. Training je nach Temperatur anpassen."));
        wissenThemaListe.add(new WissenThema("4. Phase: Lutealphase", "Dauer: 10-14 Tage\nVorgänge: Gebärmutterschleimhaut wird wieder aufgebaut\nHormone: Nach anfänglichem Anstieg fangen Progesteron- sowie Östrogenspiegel an, zu sinken\nFolgen: Energielevel, Reaktionsgeschwindigkeit, Koordination und Feinmotorik verschlechtern sich infolge der Progesteronproduktion\nTrainingsempfehlung: Trainingsintensität und -volumen reduzieren: leichteres Krafttraining, lockere Regenerationsläufe. Auf erhaltendes, nicht unbedingt aufbauendes Training setzen."));
        wissenThemaListe.add(new WissenThema("Über die App", "Diese App rechnet aus, wann welche Phase des Menstruationszyklus auftritt und gibt Tipps, wie du dein Training darauf anpassen kannst (bitte beachte aber, dass die Berechnungen für Personen, welche nicht hormonell verhüten und einen Zyklus von 28 Tagen haben, optimiert sind. Bei viel kürzeren oder längeren Zyklen treffen wahrscheinlich nur noch die Berechnungen für die Menstruationsphase zu).\nFür zyklusgesteuertes Training ist eine gute Analyse essenziell, damit Regelmässigkeiten erkennt werden können. Dabei kann dir die Tagebuch-Funktion helfen.\nAlle Informationen werden nur auf deinem Gerät gespeichert und sind für andere Apps nicht erreichbar (es sei denn, dein Handy ist gerooted) und werden bei der Deinstallation der App gelöscht.\nDiese App soll kein allwissender Coach sein und das eigene Körpergefühl ersetzen. Stattdessen ist sie in erster Linie zur Beobachtung des Menstruationszyklus da."));
    }
}