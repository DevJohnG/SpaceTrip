package com.example.spacetrip.service;

import com.example.spacetrip.entities.Logro;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface LogrosApi {
    @GET("/logros")
    Call<List<Logro>> getLogros();
}
