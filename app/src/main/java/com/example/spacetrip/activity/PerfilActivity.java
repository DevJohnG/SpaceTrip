package com.example.spacetrip.activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.spacetrip.R;
import com.example.spacetrip.entities.Jugador;
import com.example.spacetrip.service.JugadorApi;
import com.example.spacetrip.service.JugadorApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PerfilActivity extends AppCompatActivity {

    private Switch switchSound;
    private ImageView soundImageView;
    private SharedPreferences preferences;
    private static final String PREFS_NAME = "GamePrefs";
    private static final String SOUND_ENABLED_KEY = "soundEnabled";

    private Jugador jugador;
    private EditText usuarioNombretxt, passwordtxt, fechaNacimientotxt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        // Inicializar controles de sonido
        switchSound = findViewById(R.id.switchSound);
        soundImageView = findViewById(R.id.soundImage);

        preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean isSoundEnabled = preferences.getBoolean(SOUND_ENABLED_KEY, false);
        switchSound.setChecked(isSoundEnabled);
        updateSoundImage(isSoundEnabled);



        switchSound.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean(SOUND_ENABLED_KEY, isChecked);
            editor.apply();
            updateSoundImage(isChecked);
        });

        // Inicializar controles de perfil
        ImageButton btnVolver = findViewById(R.id.btnVolver);
        usuarioNombretxt = findViewById(R.id.usuarioNombre);
        passwordtxt = findViewById(R.id.contrasena);
        fechaNacimientotxt = findViewById(R.id.fechaNacimiento);

        // Deshabilitar edición al inicio
        usuarioNombretxt.setEnabled(false);
        passwordtxt.setEnabled(false);
        fechaNacimientotxt.setEnabled(false);

        // Recibir el objeto Jugador desde GameActivity
        Intent intent = getIntent();
        jugador = intent.getParcelableExtra("usuario");

        if (jugador == null) {
            Toast.makeText(this, "No se recibieron los datos del usuario", Toast.LENGTH_SHORT).show();
            return;
        }

        // Realizar la llamada a la API para obtener el jugador completo por correo
        obtenerJugadorPorCorreo(jugador.getCorreo());

        btnVolver.setOnClickListener(v -> finish());
        Button btnAyuda = findViewById(R.id.btnAyuda);
        btnAyuda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                watchYoutubeVideo("BLb8v_g8-6k");
            }
        });


    }
    private void watchYoutubeVideo(String id) {
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("https://www.youtube.com/watch?v=" + id));
        try {
            startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            startActivity(webIntent);
        }
    }



    private void updateSoundImage(boolean isSoundEnabled) {
        if (isSoundEnabled) {
            soundImageView.setImageResource(R.drawable.sonido);
        } else {
            soundImageView.setImageResource(R.drawable.sonidooff);
        }
    }

    private void obtenerJugadorPorCorreo(String correo) {
        JugadorApi jugadorApi = JugadorApiService.getApiService();
        Call<Jugador> call = jugadorApi.getJugadorByCorreo(correo);
        call.enqueue(new Callback<Jugador>() {
            @Override
            public void onResponse(Call<Jugador> call, Response<Jugador> response) {
                if (response.isSuccessful()) {
                    jugador = response.body();
                    if (jugador != null) {
                        usuarioNombretxt.setText(jugador.getUsername());
                        passwordtxt.setText(jugador.getPassword());
                        fechaNacimientotxt.setText(jugador.getFecha_nacimiento());
                    }
                } else {
                    Toast.makeText(PerfilActivity.this, "Error al obtener los datos del usuario", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Jugador> call, Throwable t) {
                Toast.makeText(PerfilActivity.this, "Error de red", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
