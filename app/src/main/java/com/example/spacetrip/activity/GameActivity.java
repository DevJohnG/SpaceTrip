package com.example.spacetrip.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.spacetrip.R;
import com.example.spacetrip.entities.Jugador;

public class GameActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;
    private SharedPreferences preferences;
    private Jugador jugador;
    private static final String PREFS_NAME = "GamePrefs";
    private static final String SOUND_ENABLED_KEY = "soundEnabled";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_screen);

        ImageButton btnRanking = findViewById(R.id.btnRanking);
        ImageButton btnAjustes = findViewById(R.id.btnConfiguraciones);
        ImageButton btnLogros = findViewById(R.id.btnLogros);
        ImageButton btntest = findViewById(R.id.btnTest);
        ImageButton playButton = findViewById(R.id.btnPlay);

        // Recibir el objeto Jugador desde LoginActivity
        Intent intent = getIntent();
        jugador = intent.getParcelableExtra("usuario");

        preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean isSoundEnabled = preferences.getBoolean(SOUND_ENABLED_KEY, false);

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GameActivity.this, MainActivity.class);
                intent.putExtra("usuario", jugador);
                startActivity(intent);
                finish();
            }
        });

        btnRanking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(GameActivity.this, RankingActivity.class);
                startActivity(i);
            }
        });

        btnAjustes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(GameActivity.this, PerfilActivity.class);
                i.putExtra("usuario", jugador);

                startActivity(i);
            }
        });

        btntest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(GameActivity.this, TestSelectionActivity.class);
                startActivity(i);
            }
        });

        btnLogros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(GameActivity.this, LogrosActivity.class);
                startActivity(i);
            }
        });

        updateSoundState(isSoundEnabled);

        ImageView coheteImageView = findViewById(R.id.cohete);
        RequestOptions requestOptions = new RequestOptions().override(40, 80);
        Glide.with(this).asGif().load(R.drawable.cohetegif).apply(requestOptions).into(coheteImageView);
    }

    private void updateSoundState(boolean isSoundEnabled) {
        if (isSoundEnabled) {
            if (mediaPlayer == null) {
                mediaPlayer = MediaPlayer.create(this, R.raw.music);
                mediaPlayer.setLooping(true);
            }
            mediaPlayer.start();
        } else {
            if (mediaPlayer != null) {
                mediaPlayer.pause();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        boolean isSoundEnabled = preferences.getBoolean(SOUND_ENABLED_KEY, true);
        updateSoundState(isSoundEnabled);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
