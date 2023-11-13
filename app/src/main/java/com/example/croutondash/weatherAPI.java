package com.example.croutondash;

import static java.lang.Math.round;

import android.content.Context;
import android.content.res.Resources;
import android.widget.ImageView;
import android.widget.TextView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class weatherAPI {
// const url = 'https://api.pirateweather.net/forecast/${key}/${latlong}?units=${unit}';

    public void fetchWeather(TextView currentTemperature, TextView currentSummary, ImageView weatherIcon) {
        String BASE_URL = "https://api.pirateweather.net/";
        Context context = currentSummary.getContext();
        Resources resources = context.getResources();
        String api_key = resources.getString(R.string.api_key);
        double latitude = Double.parseDouble(resources.getString(R.string.latitude));
        double longitude = Double.parseDouble(resources.getString(R.string.longitude));

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        weatherGET getWeather = retrofit.create(weatherGET.class);
        Call<WeatherData> call = getWeather.getCurrentWeather(api_key, latitude, longitude);

        call.enqueue(new Callback<WeatherData>() {
            @Override
            public void onResponse(Call<WeatherData> call, Response<WeatherData> response) {
                if (response.isSuccessful()) {
                    WeatherData weatherData = response.body();

                    assert weatherData != null;
                    double temperature = weatherData.getCurrent().getTemperature();
                    String summary = weatherData.getCurrent().getSummary();
                    String icon = weatherData.getCurrent().getIcon();
                    String resultingTemp = round(temperature) + "°c";

                    fetchDrawable(icon, weatherIcon);
                    currentTemperature.setText(resultingTemp);
                    currentSummary.setText(summary);
                } else {

                    currentTemperature.setText("error getting temp: " + String.valueOf(response.code()));
                    currentSummary.setText("error getting summary: " + String.valueOf(response.code()));
                }
            }
            @Override
            public void onFailure(Call<WeatherData> call, Throwable t) {
                String errorMessage = t.getMessage();

                currentTemperature.setText("error getting temp");
                currentSummary.setText(errorMessage);
            }
        });
    }
    private void fetchDrawable(String icon, ImageView weatherIcon) {
        // weather-icons by erikflowers: https://github.com/erikflowers/weather-icons
        Context context = weatherIcon.getContext();
        String iconFinal = "ic_" + icon.replace("-","_");
        int resId = context.getResources().getIdentifier(iconFinal, "drawable", context.getPackageName());
        weatherIcon.setImageResource(resId);
    }

}
