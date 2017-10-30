package com.example.dell.tourassistant.PlacePackage;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * Created by DELL on 10/13/2017.
 */

public interface PlaceApi {
    @GET
    Call<Places>getPlaceInfo(
            @Url String url
    );
}
