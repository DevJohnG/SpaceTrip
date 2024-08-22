package com.example.spacetrip.entities;

public class Planeta {
    private int idPlaneta;
    private String nombrePlaneta;
    private String descripcionPlaneta;
    private String imagenPlanetaUrl;

    // Getters y Setters
    public int getIdPlaneta() {
        return idPlaneta;
    }

    public void setIdPlaneta(int idPlaneta) {
        this.idPlaneta = idPlaneta;
    }

    public String getNombrePlaneta() {
        return nombrePlaneta;
    }

    public void setNombrePlaneta(String nombrePlaneta) {
        this.nombrePlaneta = nombrePlaneta;
    }

    public String getDescripcionPlaneta() {
        return descripcionPlaneta;
    }

    public void setDescripcionPlaneta(String descripcionPlaneta) {
        this.descripcionPlaneta = descripcionPlaneta;
    }

    public String getImagenPlanetaUrl() {
        return imagenPlanetaUrl;
    }

    public void setImagenPlanetaUrl(String imagenPlanetaUrl) {
        this.imagenPlanetaUrl = imagenPlanetaUrl;
    }
}
