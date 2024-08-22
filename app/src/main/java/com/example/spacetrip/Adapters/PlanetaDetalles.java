package com.example.spacetrip.Adapters;

public class PlanetaDetalles {
    private String nombre_astro;
    private String distancia_minima;
    private String distancia_maxima;
    private String diametro;
    private String gravedad;
    private String temperatura_promedio;
    private String presencia_de_agua;
    private String composicion;
    private String lunas;
    private String duracion_del_dia;
    private int puntaje_minimo;
    private int imagen = 0;

    public PlanetaDetalles(String nombre_astro, String distancia_minima, String distancia_maxima, String diametro, String gravedad, String temperatura_promedio, String presencia_de_agua, String composicion, String lunas, String duracion_del_dia, int puntaje_minimo, int imagen) {
        this.nombre_astro = nombre_astro;
        this.distancia_minima = distancia_minima;
        this.distancia_maxima = distancia_maxima;
        this.diametro = diametro;
        this.gravedad = gravedad;
        this.temperatura_promedio = temperatura_promedio;
        this.presencia_de_agua = presencia_de_agua;
        this.composicion = composicion;
        this.lunas = lunas;
        this.duracion_del_dia = duracion_del_dia;
        this.puntaje_minimo = puntaje_minimo;
        this.imagen = imagen;
    }

    public int getImagen() {
        return imagen;
    }

    public void setImagen(int imagen) {
        this.imagen = imagen;
    }

    public String getNombre_astro() {
        return nombre_astro;
    }

    public void setNombre_astro(String nombre_astro) {
        this.nombre_astro = nombre_astro;
    }

    public String getDistancia_minima() {
        return distancia_minima;
    }

    public void setDistancia_minima(String distancia_minima) {
        this.distancia_minima = distancia_minima;
    }

    public String getDistancia_maxima() {
        return distancia_maxima;
    }

    public void setDistancia_maxima(String distancia_maxima) {
        this.distancia_maxima = distancia_maxima;
    }

    public String getDiametro() {
        return diametro;
    }

    public void setDiametro(String diametro) {
        this.diametro = diametro;
    }

    public String getGravedad() {
        return gravedad;
    }

    public void setGravedad(String gravedad) {
        this.gravedad = gravedad;
    }

    public String getTemperatura_promedio() {
        return temperatura_promedio;
    }

    public void setTemperatura_promedio(String temperatura_promedio) {
        this.temperatura_promedio = temperatura_promedio;
    }

    public String getPresencia_de_agua() {
        return presencia_de_agua;
    }

    public void setPresencia_de_agua(String presencia_de_agua) {
        this.presencia_de_agua = presencia_de_agua;
    }

    public String getComposicion() {
        return composicion;
    }

    public void setComposicion(String composicion) {
        this.composicion = composicion;
    }

    public String getLunas() {
        return lunas;
    }

    public void setLunas(String lunas) {
        this.lunas = lunas;
    }

    public String getDuracion_del_dia() {
        return duracion_del_dia;
    }

    public void setDuracion_del_dia(String duracion_del_dia) {
        this.duracion_del_dia = duracion_del_dia;
    }

    public int getPuntaje_minimo() {
        return puntaje_minimo;
    }

    public void setPuntaje_minimo(int puntaje_minimo) {
        this.puntaje_minimo = puntaje_minimo;
    }
}
