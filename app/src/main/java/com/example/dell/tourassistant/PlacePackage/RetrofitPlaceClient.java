package com.example.dell.tourassistant.PlacePackage;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by DELL on 10/13/2017.
 */

public class RetrofitPlaceClient {
    public static Retrofit getRetrofitPlaceClient(){
        return new Retrofit.Builder()
                .baseUrl("https://maps.googleapis.com/maps/api/place/nearbysearch/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}
