package com.example.spacetrip.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.spacetrip.R;
import com.example.spacetrip.entities.Jugador;

import java.util.List;

public class JugadorAdapter extends ArrayAdapter<Jugador> {

    public JugadorAdapter(Context context, List<Jugador> jugadores) {
        super(context, 0, jugadores);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Jugador jugador = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_jugador, parent, false);
        }

        TextView usernameTextView = convertView.findViewById(R.id.usernameTextView);
        TextView puntosTotalesTextView = convertView.findViewById(R.id.puntosTotalesTextView);
        TextView topAlturaTextView = convertView.findViewById(R.id.topAlturaTextView);

        usernameTextView.setText(jugador.getUsername());
        puntosTotalesTextView.setText(String.valueOf(jugador.getPuntos_totales()));
        topAlturaTextView.setText(String.valueOf(jugador.getTop_altura()));

        return convertView;
    }
}
