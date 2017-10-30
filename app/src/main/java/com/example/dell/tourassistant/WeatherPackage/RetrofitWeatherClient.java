package com.example.dell.tourassistant.WeatherPackage;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by DELL on 10/12/2017.
 */

public class RetrofitWeatherClient {
    public  static Retrofit getRetrofitClient(){
        return new Retrofit.Builder()
                .baseUrl("https://api.weatherbit.io/v2.0/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

}
