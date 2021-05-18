package com.example.tcpsocketclient.Model.Servidor;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ClienteRetrofit {


    //private static final String BASE_URL = "http://192.168.1.7/";
    private static final String BASE_URL = "http://192.168.1.111/";


    public static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
}
