package com.example.spacetrip.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.spacetrip.R;
import com.example.spacetrip.Adapters.DetallePlanetaActivity;
import com.example.spacetrip.entities.Logro;

import java.util.List;
public class LogrosBaseAdapter extends BaseAdapter {

    Context context;
    String planetasList[];
    String distanciasList[];
    int planetasImages[];
    LayoutInflater inflater;
    public LogrosBaseAdapter(Context ctx, String[] planetasList, String[] distanciasList, int[] planetasImages){
        this.context = ctx;
        this.planetasList = planetasList;
        this.distanciasList = distanciasList;
        this.planetasImages = planetasImages;
        inflater=LayoutInflater.from (ctx);
    }


    @Override
    public int getCount() {
        return planetasList.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.activity_logroslistview, null);
        }

        TextView txtnombrePlaneta = convertView.findViewById(R.id.txtnombrePlaneta);
        TextView txtDistancia = convertView.findViewById(R.id.txtDistancia);
        ImageView planetasImage = convertView.findViewById(R.id.imageIcon);

        txtnombrePlaneta.setText(planetasList[position]);
        txtDistancia.setText(distanciasList[position]);
        planetasImage.setImageResource(planetasImages[position]);
        return convertView;
    }
}