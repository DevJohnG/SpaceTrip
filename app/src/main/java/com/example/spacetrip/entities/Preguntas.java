package com.example.spacetrip.entities;

public class Preguntas {
    private int id_pregunta;
    private String pregunta;
    private String respuesta_correcta;
    private String preguntaIncorrecta1;
    private String preguntaIncorrecta2;
    private String preguntaIncorrecta3;
    private String imagenUrl;

    // Getters y setters
    public int getId() {
        return id_pregunta;
    }

    public void setId(int id) {
        this.id_pregunta = id;
    }

    public String getPregunta() {
        return pregunta;
    }

    public void setPregunta(String pregunta) {
        this.pregunta = pregunta;
    }

    public String getRespuesta_correcta() {
        return respuesta_correcta;
    }

    public void setRespuesta_correcta(String respuesta_correcta) {
        this.respuesta_correcta = respuesta_correcta;
    }

    public String getRespuesta_incorrecta_1() {
        return preguntaIncorrecta1;
    }

    public void setRespuesta_incorrecta_1(String respuesta_incorrecta_1) {
        this.preguntaIncorrecta1 = preguntaIncorrecta1;
    }

    public String getRespuesta_incorrecta_2() {
        return preguntaIncorrecta2;
    }

    public void setRespuesta_incorrecta_2(String respuesta_incorrecta_2) {
        this.preguntaIncorrecta2 = preguntaIncorrecta2;
    }

    public String getRespuesta_incorrecta_3() {
        return preguntaIncorrecta3;
    }

    public void setRespuesta_incorrecta_3(String respuesta_incorrecta_3) {
        this.preguntaIncorrecta3 = preguntaIncorrecta3;
    }

    public String getImageUrl() {
        return imagenUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imagenUrl = imagenUrl;
    }
}
