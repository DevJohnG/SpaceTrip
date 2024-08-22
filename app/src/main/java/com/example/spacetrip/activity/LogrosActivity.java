package com.example.spacetrip.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.spacetrip.Adapters.PlanetaDetalles;
import com.example.spacetrip.Adapters.PlanetaAdapter;

import com.example.spacetrip.R;
import com.example.spacetrip.service.JugadorApi;
import com.example.spacetrip.service.JugadorApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LogrosActivity extends AppCompatActivity {

    private ListView listView;
    private PlanetaAdapter adapter;
    private List<PlanetaDetalles> planetas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_logros);

        ImageButton btnVolver = findViewById(R.id.btnVolver);

        btnVolver.setOnClickListener(v -> {
            Intent i = new Intent(LogrosActivity.this, GameActivity.class);
            startActivity(i);
        });

        listView = findViewById(R.id.customListView);
        adapter = new PlanetaAdapter(this, planetas);
        listView.setAdapter(adapter);

        cargarDatos();
    }

    private void cargarDatos() {
        JugadorApi apiService = JugadorApiService.getApiService();
        Call<List<PlanetaDetalles>> call = apiService.getPlanetas();

        call.enqueue(new Callback<List<PlanetaDetalles>>() {
            @Override
            public void onResponse(Call<List<PlanetaDetalles>> call, Response<List<PlanetaDetalles>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<PlanetaDetalles> detalles = response.body();
                    for (PlanetaDetalles detalle : detalles) {
                        int puntajeMinimo = convertirDistancia(detalle.getDistancia_minima());

                        // Aquí se asigna la imagen mapeada por el nombre del planeta
                        int imagen = mapearImagenPorNombre(detalle.getNombre_astro());
                        detalle.setImagen(imagen); // Asegúrate de que PlanetaDetalles tiene este campo

                        planetas.add(new PlanetaDetalles(
                                detalle.getNombre_astro(),
                                detalle.getDistancia_minima(),
                                detalle.getDistancia_maxima(),
                                detalle.getDiametro(),
                                detalle.getGravedad(),
                                detalle.getTemperatura_promedio(),
                                detalle.getPresencia_de_agua(),
                                detalle.getComposicion(),
                                detalle.getLunas(),
                                detalle.getDuracion_del_dia(),
                                puntajeMinimo,
                                imagen // Añadir la imagen aquí
                        ));
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(LogrosActivity.this, "Error al obtener datos", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<PlanetaDetalles>> call, Throwable t) {
                Toast.makeText(LogrosActivity.this, "Error de red", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private int convertirDistancia(String distancia) {
        return Integer.parseInt(distancia);
    }

    private int mapearImagenPorNombre(String nombre) {
        // Mapea el nombre del planeta a la imagen correspondiente
        switch (nombre.toLowerCase()) {
            case "tierra":
                return R.drawable.tierra;
            case "estación espacial internacional":
                return R.drawable.satelite;
            case "luna":
                return R.drawable.luna;
            case "mercurio":
                return R.drawable.mercurio;
            case "venus":
                return R.drawable.venus;
            case "marte":
                return R.drawable.marte;
            case "júpiter":
                return R.drawable.jupiter;
            case "saturno":
                return R.drawable.saturno;
            case "urano":
                return R.drawable.urano;
            case "neptuno":
                return R.drawable.neptuno;
            case "sol":
                return R.drawable.sol;
            default:
                return R.drawable.tierra; // Imagen por defecto en caso de no encontrar coincidencia
        }
    }
}
