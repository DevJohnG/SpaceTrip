package com.example.spacetrip.service;

import com.example.spacetrip.entities.Jugador;
import com.example.spacetrip.Adapters.PlanetaDetalles;
import com.example.spacetrip.entities.Preguntas;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface JugadorApi {
    @POST("/usuarios")
    Call<Jugador> createUsuario(@Body Jugador jugador);

    @POST("/usuarios/login")
    Call<Jugador> logearJugador(@Body Jugador jugador);

    @GET("/usuarios/findByCorreo")
    Call<Jugador> getJugadorByCorreo(@Query("correo") String correo);

    @PUT("/usuarios/{id}")
    Call<Jugador> updateUsuario(@Path("id") int id, @Body Jugador jugador);

    @GET("astro")
    Call<List<PlanetaDetalles>> getPlanetas();

    @GET("/usuarios/{id}")
    Call<Jugador> getUsuarioById(@Path("id") int id);

    @PUT("/usuarios/{id}/ActualizarDistancia")
    Call<Jugador> updateTopAltura(@Path("id") int id, @Body Jugador jugador);

    @GET("/usuarios/top10ByTopAltura")
    Call<List<Jugador>> getTop10ByTopAltura();

    @PUT("/usuarios/{correo}/updatePuntos_TotalesbyCorreo")
    Call<Jugador> updatePuntosTotalesbyCorreo(@Path("correo") String correo, @Body Jugador jugador);

    @PUT("/usuarios/{correo}/ActualizarDistanciabyCorreo")
    Call<Jugador> updateTopAlturabyCorreo(@Path("correo") String correo, @Body Jugador jugador);



}
