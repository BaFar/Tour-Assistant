package com.example.dell.tourassistant.CombinedWeather;

import android.content.SharedPreferences;
import android.preference.Preference;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.example.dell.tourassistant.R;

public class WeatherActivity extends AppCompatActivity implements WeatherHomeFragment.DetailsInterface{

    private FragmentManager fm = getSupportFragmentManager();
    private FragmentTransaction ft = fm.beginTransaction();
    private BottomNavigationView navigationView;
    private String cityName,dateTime;
    private double temp;
    private SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        navigationView = (BottomNavigationView) findViewById(R.id.weather_bottom_nav);

        double lat = 90.4786;
        double lon = 23.81435;

        try{
            lat = getIntent().getDoubleExtra("event_lattitude",0.0);
            lon = getIntent().getDoubleExtra("event_longitude",0.0);
        }catch (Exception e){
            Log.e("weatherActivity","No intent data found");
            Log.d("weatherActivity","not lat lon recveived");
        }

        preferences = getSharedPreferences("latlonSP",MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putFloat("req_lat",(float) lat);
        editor.putFloat("req_lon",(float) lon);
        editor.apply();
        editor.commit();


        final Bundle bundle= new Bundle();
        bundle.putDouble("lattitude",lat);
        bundle.putDouble("longitude",lon);
        WeatherHomeFragment weatherHomeFragment = new WeatherHomeFragment();
        weatherHomeFragment.setArguments(bundle);
        ft.add(R.id.weather_fragment_coontainer,weatherHomeFragment);
        ft.commit();
        navigationView.setSelectedItemId(R.id.nav_current);

    final double iLat = lat;
    final double iLon = lon;


        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentManager fmanager= getSupportFragmentManager();
                FragmentTransaction fTranjection = fmanager.beginTransaction();
                Fragment fragment = null;
                Bundle innerBundle = new Bundle();
                switch (item.getItemId()){
                    case R.id.nav_current:

                        innerBundle.putDouble("lattitude",preferences.getFloat("req_lat",0));
                        innerBundle.putDouble("longitude",preferences.getFloat("req_lon",0));
                        fragment = new WeatherHomeFragment();
                        fragment.setArguments(innerBundle);
                        break;
                    case R.id.nav_details:
                        fragment = new DetailsFragment();
                        innerBundle.putString("cityname",cityName);
                        innerBundle.putDouble("temperature",temp);
                        innerBundle.putString("datetime",dateTime);
                        fragment.setArguments(innerBundle);
                        break;
                    case R.id.nav_forecast:
                        innerBundle.putDouble("lattitude",preferences.getFloat("req_lat",0));
                        innerBundle.putDouble("longitude",preferences.getFloat("req_lon",0));
                        fragment = new ForecastFragment();
                        fragment.setArguments(innerBundle);
                        break;
                }
                fTranjection.replace(R.id.weather_fragment_coontainer,fragment);
                fTranjection.commit();
                return true;
            }
        });

    }


    @Override
    public void detailsContainer(String cityName, double temp, String dateTime) {

        this.cityName = cityName;
        this.temp = temp;
        this.dateTime = dateTime;
    }
}
