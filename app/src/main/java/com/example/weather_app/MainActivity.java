package com.example.weather_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.BitSet;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Url;

public class MainActivity extends AppCompatActivity {
    Button get_weather;
    EditText city_edit_text;
    ImageView img;
    TextView temperature, min_temperature, max_temperature,pressure,humidity,wind_speed,description;
    String apikey = "2470d479f938a6a7e08e53111fb6d3b9";
    //String url = "api.openweathermap.org/data/2.5/weather?q={city name}&appid={API key}";
    String baseurl = "https://api.openweathermap.org/data/2.5/";
    LinearLayoutCompat lin;
    GridLayout grid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        lin.setVisibility(View.INVISIBLE);
        grid.setVisibility(View.INVISIBLE);
    }

    protected void init() {
        get_weather = findViewById(R.id.get_weather);
        city_edit_text = findViewById(R.id.city_edit_text);
        img = findViewById(R.id.img);
        temperature = findViewById(R.id.temperature);
        min_temperature = findViewById(R.id.min_temp);
        max_temperature = findViewById(R.id.max_temp);
        pressure=findViewById(R.id.pressure);
        humidity=findViewById(R.id.humidity);
        lin = findViewById(R.id.lin);
        grid=findViewById(R.id.grid);
        wind_speed=findViewById(R.id.wind_speed);
        description=findViewById(R.id.description);
    }

    public void getWeather(View view) {
        if(TextUtils.isEmpty(city_edit_text.getText().toString()))
        {
            Toast.makeText(getApplicationContext(),"Enter a city name first",Toast.LENGTH_LONG).show();
            return;
        }
        Retrofit retrofit = new Retrofit.Builder().
                baseUrl(baseurl).
                addConverterFactory(GsonConverterFactory.create()).build();
        weatherapi weather_api = retrofit.create(weatherapi.class);
        Call<SerializableObjects> serializableObjectsCall =
                weather_api.getweather(city_edit_text.getText().toString().trim(), apikey);
        serializableObjectsCall.enqueue(new Callback<SerializableObjects>() {
            @Override
            public void onResponse(Call<SerializableObjects> call, Response<SerializableObjects> response) {
                if (response.code() == 404) {
                    Toast.makeText(getApplicationContext(), "Invalid city name", Toast.LENGTH_LONG).show();
                } else if (!response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), response.code(), Toast.LENGTH_LONG).show();
                } else {
                    lin.setVisibility(View.VISIBLE);
                    grid.setVisibility(View.VISIBLE);
                    SerializableObjects serializableObjects = response.body();
                    WeatherModal weatherModal = serializableObjects.getWeatherModal();
                    Double temp = weatherModal.getTemp();
                    int temp_celcius = (int) (temp - 273.15);
                    temperature.setText(String.valueOf(temp_celcius) + "°C");
                    temp = weatherModal.getTempMin();
                    temp_celcius = (int) (temp - 273.15);
                    min_temperature.setText("Min temp: "+String.valueOf(temp_celcius) + "°C");
                    temp = weatherModal.getTempMax();
                    temp_celcius = (int) (temp - 273.15);
                    max_temperature.setText("Max temp: "+String.valueOf(temp_celcius) + "°C");
                    pressure.setText(String.valueOf(weatherModal.getPressure()));
                    humidity.setText(String.valueOf(weatherModal.getHumidity()));
                    Wind wind=serializableObjects.getWind();
                    wind_speed.setText(String.valueOf(wind.getSpeed()));
                    List<weather> weathers=serializableObjects.getWeathers();
                    String iconUrl = "http://openweathermap.org/img/w/" +weathers.get(0).getIcon()+ ".png";
                    Picasso.get().load(iconUrl).into(img);
                    description.setText(weathers.get(0).getDescription());
                }
            }

            @Override
            public void onFailure(Call<SerializableObjects> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

}