package com.example.weather_app;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface weatherapi {
    @GET("weather")
    Call<SerializableObjects> getweather(@Query("q") String city_name,
                                         @Query("appid") String apikey);
}
