package com.example.dell.tourassistant.CombinedWeather.DailyWeather;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * Created by DELL on 11/1/2017.
 */
/*https://api.weatherbit.io/v2.0/forecast/daily?key=21580262673342e28e1c87639965a4e8&lat=38.123&lon=-78.543*/

public interface DailyWeatherClient {
    @GET
    Call<DailyWeather> getDailyWeather(
            @Url String url
    );
    public static final Retrofit dailyRetrofitClient = new Retrofit.Builder()
            .baseUrl("https://api.weatherbit.io/v2.0/forecast/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
}
