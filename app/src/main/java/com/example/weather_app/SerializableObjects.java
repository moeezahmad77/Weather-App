package com.example.weather_app;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SerializableObjects {
    @SerializedName("main")
    WeatherModal weatherModal;

    public WeatherModal getWeatherModal() {
        return weatherModal;
    }

    public void setWeatherModal(WeatherModal weatherModal) {
        this.weatherModal = weatherModal;
    }

    @SerializedName("wind")
    Wind wind;

    public Wind getWind() {
        return wind;
    }

    public void setWind(Wind wind) {
        this.wind = wind;
    }

    @SerializedName("weather")
    List<weather> weathers;

    public List<weather> getWeathers() {
        return weathers;
    }

    public void setWeathers(List<weather> weathers) {
        this.weathers = weathers;
    }

}
