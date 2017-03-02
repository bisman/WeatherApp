package com.igorbelogubov.weather;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.igorbelogubov.weather.database.DataBase;
import com.igorbelogubov.weather.entity.WeatherApi;
import com.igorbelogubov.weather.view.CustomInfoWindow;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private EditText editTextCity;
    private Button searchCity;
    private GoogleMap mMap;
    private DataBase dataBase;
    private String name, description, icon, temp, tempMin, tempMax, pressure, humidity, windSpeed, windDeg;
    private Double lon, lat;

    public static final String API_KEY = "88776e3f15572c7b5b54886489efa88a";
    public static final String TAG = "Log";
    public static final String BASE_URL = "http://api.openweathermap.org/";

    RxJavaCallAdapterFactory rxAdapter = RxJavaCallAdapterFactory.create();

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(rxAdapter)
            .build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dataBase = new DataBase(this);
        dataBase.open();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        editTextCity = (EditText) findViewById(R.id.editTextCity);
        searchCity = (Button) findViewById(R.id.buttonSearch);
        searchCity.setOnClickListener(v -> setWeatherData());
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(50, 30)));
        mMap.setInfoWindowAdapter(new CustomInfoWindow(this, getLayoutInflater()));
    }

    private void setWeatherData() {
        dataBase.delData();
        mMap.clear();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
        String city = editTextCity.getText().toString();

        WeatherApi weatherApi = retrofit.create(WeatherApi.class);
        weatherApi.getWeatherData(city, API_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(weatherData -> {
                            name = city;
                            lon = weatherData.getCoord().getLon();
                            lat = weatherData.getCoord().getLat();
                            description = weatherData.getWeather().get(0).getDescription();
                            temp = String.format("%.1f", weatherData.getMain().getTemp() - 273.15) + " °C";
                            pressure = weatherData.getMain().getPressure() + " hPa";
                            humidity = weatherData.getMain().getHumidity() + "%";
                            tempMin = String.format("%.1f", weatherData.getMain().getTempMin() - 273.15) + " °C";
                            tempMax = String.format("%.1f", weatherData.getMain().getTempMax() - 273.15) + " °C";
                            windSpeed = weatherData.getWind().getSpeed() + " m/sec";
                            windDeg = String.valueOf(weatherData.getWind().getDeg());
                            icon = weatherData.getWeather().get(0).getIcon();

                            dataBase.addRec(name, lon, lat, description, temp, pressure, humidity, tempMin,
                                    tempMax, windSpeed, windDeg, icon);

                            Cursor c = dataBase.getData();
                            if (c.moveToFirst()) {
                                int lonIndex = c.getColumnIndex(DataBase.COLUMN_LON);
                                int latIndex = c.getColumnIndex(DataBase.COLUMN_LAT);
                                LatLng latLng = new LatLng(c.getDouble(latIndex), c.getDouble(lonIndex));
                                Marker m = mMap.addMarker(new MarkerOptions().position(latLng));
                                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                                m.showInfoWindow();
                            }
                            Log.e(TAG, String.valueOf(weatherData));
                        },
                        throwable -> {
                            Toast.makeText(this, "Error", Toast.LENGTH_LONG).show();
                            Log.e(TAG, "Error: " + String.valueOf(throwable));
                        });
    }
}

