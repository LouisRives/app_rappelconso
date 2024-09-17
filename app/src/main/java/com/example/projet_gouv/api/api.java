package com.example.projet_gouv.api;
import com.example.projet_gouv.data.RappelResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface api {
    @GET("rappelconso0/records")
    Call<RappelResponse> getRappels(@Query("limit") int limit, @Query("where") String where);
}

