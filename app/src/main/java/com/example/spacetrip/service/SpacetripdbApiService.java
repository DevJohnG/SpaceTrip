package com.example.spacetrip.service;


import com.example.spacetrip.entities.Preguntas;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;

public interface SpacetripdbApiService {

    @GET("preguntas")
    Call<ArrayList<Preguntas>> getPreguntas();
}
