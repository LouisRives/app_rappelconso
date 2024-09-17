package com.example.projet_gouv.api;

import com.example.projet_gouv.data.RappelResponse;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class apiImplement implements api {
    private final api service;

    public apiImplement() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://data.economie.gouv.fr/api/explore/v2.0/catalog/datasets/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(api.class);
    }

    @Override
    public Call<RappelResponse> getRappels(int limit, String where) {
        return service.getRappels(limit, where);
    }
}
