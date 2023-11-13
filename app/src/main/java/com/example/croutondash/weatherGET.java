package com.example.croutondash;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Using PirateWeather API
 */

public interface weatherGET {
    @GET("forecast/{API_KEY}/{latitude},{longitude}?units=ca")
    Call<WeatherData> getCurrentWeather(
            @Path("API_KEY") String apiKey,
            @Path("latitude") double latitude,
            @Path("longitude") double longitude
    );
}
