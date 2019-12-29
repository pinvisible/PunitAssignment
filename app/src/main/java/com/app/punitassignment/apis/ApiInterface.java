package com.app.punitassignment.apis;

import com.app.punitassignment.models.Hits;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface ApiInterface {
   @GET("v1/search_by_date")
   Call<Hits> getHits(@QueryMap Map<String, String> queryString);
}
