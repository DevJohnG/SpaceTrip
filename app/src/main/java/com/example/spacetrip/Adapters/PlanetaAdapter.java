package com.example.spacetrip.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.spacetrip.R;
import com.example.spacetrip.entities.Jugador;
import com.example.spacetrip.service.JugadorApi;
import com.example.spacetrip.service.JugadorApiService;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlanetaAdapter extends ArrayAdapter<PlanetaDetalles> {
    private static final String PREFS_NAME = "GamePrefs";
    private static final String HEIGHT_KEY = "height";

    public PlanetaAdapter(Context context, List<PlanetaDetalles> planetaDetalles) {
        super(context, 0, planetaDetalles);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_logroslistview, parent, false);
        }

        PlanetaDetalles planetaDetalles = getItem(position);

        ImageView imageIcon = convertView.findViewById(R.id.imageIcon);
        TextView txtnombrePlaneta = convertView.findViewById(R.id.txtnombrePlaneta);
        TextView txtDistancia = convertView.findViewById(R.id.txtDistancia);
        Button btnVerLogro = convertView.findViewById(R.id.btnVerLogro);

        txtnombrePlaneta.setText(planetaDetalles.getNombre_astro());
        txtDistancia.setText(planetaDetalles.getDistancia_minima());
        imageIcon.setImageResource(planetaDetalles.getImagen()); // Aquí se configura la imagen

        btnVerLogro.setOnClickListener(v -> {
            if (cumpleCondicionParaVerDetalles(planetaDetalles.getPuntaje_minimo())) {
                Intent intent = new Intent(getContext(), DetallePlanetaActivity.class);
                intent.putExtra("nombrePlaneta", planetaDetalles.getNombre_astro());
                intent.putExtra("distanciaMin", planetaDetalles.getDistancia_minima());
                intent.putExtra("distanciaMax", planetaDetalles.getDistancia_maxima());
                intent.putExtra("diametro", planetaDetalles.getDiametro());
                intent.putExtra("gravedad", planetaDetalles.getGravedad());
                intent.putExtra("temperatura", planetaDetalles.getTemperatura_promedio());
                intent.putExtra("agua", planetaDetalles.getPresencia_de_agua());
                intent.putExtra("composicion", planetaDetalles.getComposicion());
                intent.putExtra("lunas", planetaDetalles.getLunas());
                intent.putExtra("duracionDia", planetaDetalles.getDuracion_del_dia());
                intent.putExtra("imagen", planetaDetalles.getImagen()); // Asegúrate de pasar la imagen aquí
                getContext().startActivity(intent);
            } else {
                Toast.makeText(getContext(), "No cumples con la condición para ver los detalles de este planeta.", Toast.LENGTH_SHORT).show();
            }
        });
        return convertView;
    }


    private boolean cumpleCondicionParaVerDetalles(int puntajeMinimo) {
        int puntajeJugador = obtenerPuntajeJugador();
        return puntajeJugador >= puntajeMinimo;
    }

    private int obtenerPuntajeJugador() {
        SharedPreferences preferences = getContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return preferences.getInt(HEIGHT_KEY, 0);
    }
}
