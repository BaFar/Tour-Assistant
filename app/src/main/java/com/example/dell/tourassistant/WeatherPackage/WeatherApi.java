package com.example.dell.tourassistant.WeatherPackage;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * Created by DELL on 10/12/2017.
 */

public interface WeatherApi {
    @GET
    Call<Wether> getWeatherInfo(
            @Url String url
    );
}
