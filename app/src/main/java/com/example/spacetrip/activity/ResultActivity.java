package com.example.spacetrip.activity;

import android.media.MediaPlayer;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.spacetrip.R;

public class ResultActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;
    private MediaPlayer buttonClickSound;
    private MediaPlayer efect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        //aquí se maneja la música de fondo
        efect = MediaPlayer.create(this, R.raw.resultadoefecto);
        efect.start();


        mediaPlayer = MediaPlayer.create(this, R.raw.resultado);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();

        TextView resultTextView = findViewById(R.id.result_text);
        Button retryButton = findViewById(R.id.retry_button);
        ImageButton exitButton = findViewById(R.id.exit_button);

        buttonClickSound = MediaPlayer.create(this, R.raw.presionar);

        Intent intent = getIntent();
        int correctAnswers = intent.getIntExtra("CORRECT_ANSWERS", 0);
        int totalQuestions = intent.getIntExtra("TOTAL_QUESTIONS", 0);

        String resultMessage;
        if (correctAnswers == totalQuestions) {
            resultMessage = "\n¡Excelente! Eres todo un viajero espacial\n\nHas completado este sistema planetario.";
            retryButton.setVisibility(View.GONE);
        } else if (correctAnswers >= 6 && correctAnswers < totalQuestions) {
            resultMessage = "\n¡Ya casi! Explora y descubre más astros para obtener sus logros y aprender más sobre ellos.";
            retryButton.setVisibility(View.VISIBLE);
        } else {
            resultMessage = "\n¡Tú puedes! Explora para descubrir más astros y recuerda visitar los logros para aprender más sobre ellos.";
            retryButton.setVisibility(View.VISIBLE);
        }
        resultTextView.setText(String.format("REPORTE:\n\nRespuestas correctas: %d/%d\n%s", correctAnswers, totalQuestions, resultMessage));

        retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonClickSound.start();
                Intent retryIntent = new Intent(ResultActivity.this, TestActivity.class);
                startActivity(retryIntent);
                finish();
            }
        });

        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonClickSound.start();
                Intent backIntent = new Intent(ResultActivity.this, TestSelectionActivity.class);
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
        if (efect != null) {
            efect.release();
            efect = null;
        }
    }
}
