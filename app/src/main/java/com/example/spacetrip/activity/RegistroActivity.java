package com.example.spacetrip.activity;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.spacetrip.R;
import com.example.spacetrip.entities.Jugador;
import com.example.spacetrip.service.JugadorApiService;
import com.example.spacetrip.service.JugadorApi;


import java.io.IOException;
import java.text.SimpleDateFormat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistroActivity extends AppCompatActivity {
    private EditText usernameEditText, correoEditText, passwordEditText, passwordVerificarEditText;
    private EditText edadtxt;
    private Button registrarButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        inicializarControles();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");

        registrarButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString();
                String correo = correoEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String passwordVerificar = passwordVerificarEditText.getText().toString();
                String fechaNacimientoStr = edadtxt.getText().toString();

                if (username.isEmpty() || correo.isEmpty() || password.isEmpty() || passwordVerificar.isEmpty()) {
                    Toast.makeText(RegistroActivity.this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!password.equals(passwordVerificar)) {
                    Toast.makeText(RegistroActivity.this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                    return;
                }


                Jugador jugador = new Jugador(username, correo, password, fechaNacimientoStr);

                JugadorApi jugadorApi = JugadorApiService.getApiService();
                Call<Jugador> call = jugadorApi.createUsuario(jugador);

                call.enqueue(new Callback<Jugador>() {
                    @Override
                    public void onResponse(Call<Jugador> call, Response<Jugador> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(RegistroActivity.this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(RegistroActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<Jugador> call, Throwable t) {
                        if (t instanceof IOException) {
                            Toast.makeText(RegistroActivity.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(RegistroActivity.this, "Error desconocido: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        ImageButton btnVolver = findViewById (R.id.btnVolver);

        btnVolver.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                Intent i = new Intent (RegistroActivity.this , LoginActivity.class);
                startActivity (i);
            }
        });
    }

    private void inicializarControles(){
        usernameEditText = (EditText) findViewById(R.id.txtnombreUsuario);
        correoEditText = (EditText) findViewById(R.id.txtCorreo);
        passwordEditText = (EditText) findViewById(R.id.txtcontraseña);
        passwordVerificarEditText = (EditText) findViewById(R.id.txtcontraseñaVerificar);
        edadtxt = (EditText) findViewById(R.id.txtEdad);
        registrarButton = (Button) findViewById(R.id.btnCrearCuenta);
    }

}