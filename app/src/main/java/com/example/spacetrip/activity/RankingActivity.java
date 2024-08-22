package com.example.spacetrip.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.spacetrip.R;
import com.example.spacetrip.adapter.JugadorAdapter;
import com.example.spacetrip.entities.Jugador;
import com.example.spacetrip.service.JugadorApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RankingActivity extends AppCompatActivity {

    private ListView highScoreLV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ranking);

        ImageButton btnVolver = findViewById(R.id.btnVolver);
        highScoreLV = findViewById(R.id.highScoreLV);

        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RankingActivity.this, GameActivity.class);
                startActivity(i);
            }
        });

        loadTop10ByTopAltura();
    }

    private void loadTop10ByTopAltura() {
        Call<List<Jugador>> call = JugadorApiService.getApiService().getTop10ByTopAltura();
        call.enqueue(new Callback<List<Jugador>>() {
            @Override
            public void onResponse(Call<List<Jugador>> call, Response<List<Jugador>> response) {
                if (response.isSuccessful()) {
                    List<Jugador> jugadores = response.body();
                    JugadorAdapter adapter = new JugadorAdapter(RankingActivity.this, jugadores);
                    highScoreLV.setAdapter(adapter);
                } else {
                    Toast.makeText(RankingActivity.this, "Error al obtener los datos", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Jugador>> call, Throwable t) {
                Toast.makeText(RankingActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
