package com.example.spacetrip.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.widget.TextView;

import com.example.spacetrip.R;
import com.example.spacetrip.service.JugadorApi;
import com.example.spacetrip.service.JugadorApiService;
import com.example.spacetrip.entities.Jugador;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.content.Intent;

import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements GameView.GameControlListener {

    private GameView gameView;
    private TextView heightTextView;
    private Button retryButton;
    private Button navigateButton;
    private JugadorApi jugadorApi;
    private SharedPreferences preferences;
    private static final String PREFS_NAME = "GamePrefs";
    private static final String HEIGHT_KEY = "height";
    private Jugador jugador;

    // Sensores y gestión
    private SensorManager sensorManager;
    private Sensor accelerometerSensor;
    private SensorEventListener sensorListener;

    // Posición de la nave
    private int shipX, shipY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializa SharedPreferences
        preferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        jugadorApi = JugadorApiService.getApiService();

        gameView = findViewById(R.id.gameSurfaceView);
        heightTextView = findViewById(R.id.heightTextView);
        retryButton = findViewById(R.id.retryButton);
        navigateButton = findViewById(R.id.navigateButton);

        // Obtener el objeto Jugador desde el Intent
        Intent intent = getIntent();
        jugador = intent.getParcelableExtra("usuario");

        // Configura el acelerómetro
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        if (accelerometerSensor != null) {
            sensorListener = new SensorEventListener() {
                @Override
                public void onSensorChanged(SensorEvent event) {
                    float axisX = event.values[0];
                    float axisY = event.values[1];

                    float sensitivity = 0.5f;
                    int movementSpeed = 10;

                    shipX -= axisX * sensitivity * movementSpeed;
                    shipY += axisY * sensitivity * movementSpeed;

                    shipX = Math.max(0, Math.min(gameView.getWidth() - gameView.getShipWidth(), shipX));
                    shipY = Math.max(0, Math.min(gameView.getHeight() - gameView.getShipHeight(), shipY));

                    gameView.updateShipPosition(shipX, shipY);
                }

                @Override
                public void onAccuracyChanged(Sensor sensor, int accuracy) {
                }
            };

            sensorManager.registerListener(sensorListener, accelerometerSensor, SensorManager.SENSOR_DELAY_GAME);
        }

        gameView.setOnHeightChangedListener(new GameView.OnHeightChangedListener() {
            @Override
            public void onHeightChanged(final int height) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        heightTextView.setText("Height: " + height + " meters");

                        // Guardar el height en SharedPreferences
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putInt(HEIGHT_KEY, height);
                        editor.apply();
                    }
                });
            }
        });

        gameView.setGameControlListener(this);

        retryButton.setOnClickListener(v -> {
            retryButton.setVisibility(View.GONE);
            navigateButton.setVisibility(View.GONE);
            gameView.resetGame();
        });

        navigateButton.setOnClickListener(v -> {
            Intent intentNavigate = new Intent(MainActivity.this, GameActivity.class);
            startActivity(intentNavigate);
            finish();
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (sensorManager != null && sensorListener != null) {
            sensorManager.unregisterListener(sensorListener);
        }
        gameView.releaseMediaPlayers();
    }

    @Override
    public void onGameOver() {
        runOnUiThread(() -> {
            retryButton.setVisibility(View.VISIBLE);
            navigateButton.setVisibility(View.VISIBLE);
            actualizarTopAltura();
        });
    }

    /* no compara si la altura alcanzada en el juego es mayor a la que esrta almacenada,
    * se debe verificar eso y hacer la consulta a la bd para comparar estos valores y determinar
    * si se actualiza o no la db. */

    private void actualizarTopAltura() {
        int height = preferences.getInt(HEIGHT_KEY, 0);
        Log.d("MainActivity", "Altura a actualizar: " + height);

        if (jugador != null) {
            Log.d("MainActivity", "Correo del jugador: " + jugador.getCorreo());
            jugador.setTop_altura(height);

            Call<Jugador> call = jugadorApi.updateTopAlturabyCorreo(jugador.getCorreo(), jugador); // Utiliza el nuevo endpoint
            call.enqueue(new Callback<Jugador>() {
                @Override
                public void onResponse(Call<Jugador> call, Response<Jugador> response) {
                    if (response.isSuccessful()) {
                        mostrarToastEnUiThread("Altura actualizada correctamente");
                        Log.d("MainActivity", "Altura actualizada correctamente");
                    } else {
                        mostrarToastEnUiThread("Error al actualizar la altura");
                        Log.d("MainActivity", "Error al actualizar la altura: " + response.message());
                    }
                }

                @Override
                public void onFailure(Call<Jugador> call, Throwable t) {
                    mostrarToastEnUiThread("Error de red al actualizar la altura");
                    Log.d("MainActivity", "Error de red al actualizar la altura: " + t.getMessage());
                }
            });
        } else {
            mostrarToastEnUiThread("Jugador es null");
            Log.d("MainActivity", "Jugador es null");
        }
    }

    private void mostrarToastEnUiThread(String mensaje) {
        new Handler(Looper.getMainLooper()).post(() -> Toast.makeText(MainActivity.this, mensaje, Toast.LENGTH_SHORT).show());
    }
}
