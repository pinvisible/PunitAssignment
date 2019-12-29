package com.app.punitassignment.apis;

import com.app.punitassignment.BuildConfig;
import com.google.gson.Gson;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitCall {
   public static String BASE_URL = "https://hn.algolia.com/api/";

   public static Retrofit getRetrofiCall() {

      HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
      logging.level(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);

      OkHttpClient okHttpClient = new OkHttpClient.Builder()
              .addInterceptor(logging)
              .build();
      Gson gson = new Gson().newBuilder().setLenient().create();

      Retrofit retrofit = new Retrofit.Builder()
              .baseUrl(BASE_URL)
              .client(okHttpClient)
              .addConverterFactory(GsonConverterFactory.create(gson))
              .build();
      return retrofit;
   }
}
