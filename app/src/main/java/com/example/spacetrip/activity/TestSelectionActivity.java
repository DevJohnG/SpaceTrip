package com.example.spacetrip.activity;

import android.media.MediaPlayer;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.spacetrip.R;

public class TestSelectionActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;
    private MediaPlayer buttonClickSound;
    private MediaPlayer incorrectSound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_selection);

        //aquí se maneja la música de fondo
        mediaPlayer = MediaPlayer.create(this, R.raw.resultado);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();

        buttonClickSound = MediaPlayer.create(this, R.raw.presionar);
        incorrectSound = MediaPlayer.create(this, R.raw.incorrecto);

        Button test1Button = findViewById(R.id.test1_button);
        Button test2Button = findViewById(R.id.test2_button);
        Button test3Button = findViewById(R.id.test3_button);
        ImageButton btnregresar = findViewById(R.id.exit_button);

        test1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonClickSound.start();
                test1Button.setVisibility(View.INVISIBLE);
                Intent intent = new Intent(TestSelectionActivity.this, TestActivity.class);
                intent.putExtra("TEST_ID", 1);
                startActivity(intent);
            }
        });

        test2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                incorrectSound.start();
            }
        });

        test3Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                incorrectSound.start();
            }
        });

        btnregresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonClickSound.start();
                Intent backIntent = new Intent(TestSelectionActivity.this, GameActivity.class);
                startActivity(backIntent);
                finish();
            }
        });


    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mediaPlayer != null) {
            mediaPlayer.start();
        }
        Button test1Button = findViewById(R.id.test1_button);
        test1Button.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        if (buttonClickSound != null) {
            buttonClickSound.release();
            buttonClickSound = null;
        }
        if (incorrectSound != null) {
            incorrectSound.release();
            incorrectSound = null;
        }
    }

}