package com.example.spacetrip.entities;

public class Satelite {
    private int idSatelite;
    private String nombreSatelite;
    private String descripcionSatelite;
    private String imagenSateliteUrl;
    private int idPlaneta;

    // Getters y Setters
    public int getIdSatelite() {
        return idSatelite;
    }

    public void setIdSatelite(int idSatelite) {
        this.idSatelite = idSatelite;
    }

    public String getNombreSatelite() {
        return nombreSatelite;
    }

    public void setNombreSatelite(String nombreSatelite) {
        this.nombreSatelite = nombreSatelite;
    }

    public String getDescripcionSatelite() {
        return descripcionSatelite;
    }

    public void setDescripcionSatelite(String descripcionSatelite) {
        this.descripcionSatelite = descripcionSatelite;
    }

    public String getImagenSateliteUrl() {
        return imagenSateliteUrl;
    }

    public void setImagenSateliteUrl(String imagenSateliteUrl) {
        this.imagenSateliteUrl = imagenSateliteUrl;
    }

    public int getIdPlaneta() {
        return idPlaneta;
    }

    public void setIdPlaneta(int idPlaneta) {
        this.idPlaneta = idPlaneta;
    }
}