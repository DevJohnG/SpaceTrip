package com.example.spacetrip.entities;

public class Logro {
    private int id_logro;
    private String nombre_logro;
    private String distancia_alcanzada;
    private String imagen_logro_url;

    // Getters and setters
    public int getId_logro() {
        return id_logro;
    }

    public void setId_logro(int id_logro) {
        this.id_logro = id_logro;
    }

    public String getNombre_logro() {
        return nombre_logro;
    }

    public void setNombre_logro(String nombre_logro) {
        this.nombre_logro = nombre_logro;
    }

    public String getDistancia_alcanzada() {
        return distancia_alcanzada;
    }

    public void setDistancia_alcanzada(String distancia_alcanzada) {
        this.distancia_alcanzada = distancia_alcanzada;
    }

    public String getImagen_logro_url() {
        return imagen_logro_url;
    }

    public void setImagen_logro_url(String imagen_logro_url) {
        this.imagen_logro_url = imagen_logro_url;
    }
}
