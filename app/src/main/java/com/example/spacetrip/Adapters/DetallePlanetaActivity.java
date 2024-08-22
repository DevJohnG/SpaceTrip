package com.example.spacetrip.Adapters;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;


import com.example.spacetrip.R;

public class DetallePlanetaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detalle_planeta);

        Intent intent = getIntent();
        int imagen = intent.getIntExtra("imagen", R.drawable.sol); // Aquí se obtiene la imagen
        String nombrePlaneta = intent.getStringExtra("nombrePlaneta");
        String distanciaMin = intent.getStringExtra("distanciaMin");
        String distanciaMax = intent.getStringExtra("distanciaMax");
        String diametro = intent.getStringExtra("diametro");
        String gravedad = intent.getStringExtra("gravedad");
        String temperatura = intent.getStringExtra("temperatura");
        String agua = intent.getStringExtra("agua");
        String composicion = intent.getStringExtra("composicion");
        String lunas = intent.getStringExtra("lunas");
        String duracionDia = intent.getStringExtra("duracionDia");

        // Actualiza las vistas con los datos recibidos
        ImageView imageIconDetalle = findViewById(R.id.imgPlanetaDetalle);
        imageIconDetalle.setImageResource(imagen); // Se establece la imagen correcta

        TextView txtNombreDetalle = findViewById(R.id.txtNombreDetalle);
        txtNombreDetalle.setText(nombrePlaneta);

        TextView txtDistanciaMinDetalle = findViewById(R.id.txtDistanciaMinDetalle);
        txtDistanciaMinDetalle.setText(distanciaMin);

        TextView txtDistanciaMaxDetalle = findViewById(R.id.txtDistanciaMaxDetalle);
        txtDistanciaMaxDetalle.setText(distanciaMax);

        TextView txtDiametroDetalle = findViewById(R.id.txtDiametroDetalle);
        txtDiametroDetalle.setText(diametro);

        TextView txtGravedadDetalle = findViewById(R.id.txtGravedadDetalle);
        txtGravedadDetalle.setText(gravedad);

        TextView txtTemperaturaDetalle = findViewById(R.id.txtTemperaturaDetalle);
        txtTemperaturaDetalle.setText(temperatura);

        TextView txtAguaDetalle = findViewById(R.id.txtAguaDetalle);
        txtAguaDetalle.setText(agua);

        TextView txtComposicionDetalle = findViewById(R.id.txtComposicionDetalle);
        txtComposicionDetalle.setText(composicion);

        TextView txtLunasDetalle = findViewById(R.id.txtLunasDetalle);
        txtLunasDetalle.setText(lunas);

        TextView txtDuracionDiaDetalle = findViewById(R.id.txtDuracionDiaDetalle);
        txtDuracionDiaDetalle.setText(duracionDia);

        ImageButton btnVolver = findViewById(R.id.btnVolver);
        btnVolver.setOnClickListener(v -> finish());
    }

}