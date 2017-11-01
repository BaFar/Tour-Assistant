package com.example.dell.tourassistant.CombinedWeather.CurrentWeatherPackage;

import com.example.dell.tourassistant.CombinedWeather.CurrentWeatherPackage.CurrentWeather;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * Created by DELL on 10/30/2017.
 */

/*https://api.weatherbit.io/v2.0/currently?key=21580262673342e28e1c87639965a4e8&lat=38.123&lon=-78.543*/
public interface CurrentWeatherClient {
    @GET
    Call<CurrentWeather> getCurrentWeather(
            @Url String url
    );
    public static  final Retrofit currentRetrofitClient = new Retrofit.Builder()
            .baseUrl("https://api.weatherbit.io/v2.0/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
}
