package com.igorbelogubov.weather.entity;

import com.igorbelogubov.weather.entity.data.WeatherData;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface WeatherApi {
    @GET("data/2.5/weather?")
    Observable<WeatherData> getWeatherData(@Query("q") String city, @Query("APPID") String key);
}
