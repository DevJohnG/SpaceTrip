package com.example.spacetrip.entities;

import android.os.Parcel;
import android.os.Parcelable;

import java.time.LocalDateTime;

public class Jugador implements Parcelable {

    private int id_jugador;
    private String username;
    private String correo;
    private String password;
    private int puntos_totales, top_altura;
    private boolean isAdmin;
    private LocalDateTime ultima_actividad;
    private String fecha_nacimiento;

    public Jugador() {}

    public Jugador(String username, String correo, String password, String fecha_nacimiento) {
        this.username = username;
        this.correo = correo;
        this.password = password;
        this.fecha_nacimiento = fecha_nacimiento;
    }

    protected Jugador(Parcel in) {
        id_jugador = in.readInt();
        username = in.readString();
        correo = in.readString();
        password = in.readString();
        puntos_totales = in.readInt();
        top_altura = in.readInt();
        isAdmin = in.readByte() != 0;
        fecha_nacimiento = in.readString();
        // Handle LocalDateTime conversion (not directly supported by Parcel)
    }

    public static final Creator<Jugador> CREATOR = new Creator<Jugador>() {
        @Override
        public Jugador createFromParcel(Parcel in) {
            return new Jugador(in);
        }

        @Override
        public Jugador[] newArray(int size) {
            return new Jugador[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id_jugador);
        dest.writeString(username);
        dest.writeString(correo);
        dest.writeString(password);
        dest.writeInt(puntos_totales);
        dest.writeInt(top_altura);
        dest.writeByte((byte) (isAdmin ? 1 : 0));
        dest.writeString(fecha_nacimiento);
        // Handle LocalDateTime conversion (not directly supported by Parcel)
    }

    // Getters y setters

    public int getId_jugador() {
        return id_jugador;
    }

    public void setId_jugador(int id_jugador) {
        this.id_jugador = id_jugador;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getPuntos_totales() {
        return puntos_totales;
    }

    public void setPuntos_totales(int puntos_totales) {
        this.puntos_totales = puntos_totales;
    }

    public int getTop_altura() {
        return top_altura;
    }

    public void setTop_altura(int top_altura) {
        this.top_altura = top_altura;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public LocalDateTime getUltima_actividad() {
        return ultima_actividad;
    }

    public void setUltima_actividad(LocalDateTime ultima_actividad) {
        this.ultima_actividad = ultima_actividad;
    }

    public String getFecha_nacimiento() {
        return fecha_nacimiento;
    }

    public void setFecha_nacimiento(String fecha_nacimiento) {
        this.fecha_nacimiento = fecha_nacimiento;
    }
}
