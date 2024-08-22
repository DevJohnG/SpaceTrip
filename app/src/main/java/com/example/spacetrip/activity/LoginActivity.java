package com.example.spacetrip.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.spacetrip.R;
import com.example.spacetrip.entities.Jugador;
import com.example.spacetrip.service.JugadorApiService;
import com.example.spacetrip.service.JugadorApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText usuarioCuenta, contrasenaCuenta;
    private Button btnIngresar, btnCrearCuenta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usuarioCuenta = findViewById(R.id.usuarioCuenta);
        contrasenaCuenta = findViewById(R.id.contrasenaCuenta);
        btnIngresar = findViewById(R.id.btnIngresar);
        btnCrearCuenta = findViewById(R.id.btnCrearCuenta);

        btnIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String correo = usuarioCuenta.getText().toString();
                String password = contrasenaCuenta.getText().toString();
                login(correo, password);
            }
        });

        btnCrearCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegistroActivity.class);
                startActivity(intent);
            }
        });
    }

    private void login(String correo, String password) {
        JugadorApi jugadorApi = JugadorApiService.getApiService();

        Jugador jugador = new Jugador();
        jugador.setCorreo(correo);
        jugador.setPassword(password);

        Call<Jugador> call = jugadorApi.logearJugador(jugador);
        call.enqueue(new Callback<Jugador>() {
            @Override
            public void onResponse(Call<Jugador> call, Response<Jugador> response) {
                if (response.isSuccessful()) {
                    Jugador jugadorLogueado = response.body();
                    // Maneja el jugador logueado
                    Intent intent = new Intent(LoginActivity.this, GameActivity.class);
                    intent.putExtra("usuario", jugadorLogueado);
                    startActivity(intent);
                } else {
                    Toast.makeText(LoginActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Jugador> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "An error occurred", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
