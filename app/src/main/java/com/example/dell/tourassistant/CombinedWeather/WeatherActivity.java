package com.example.dell.tourassistant.CombinedWeather;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.dell.tourassistant.R;

public class WeatherActivity extends AppCompatActivity {

    private FragmentManager fm = getSupportFragmentManager();
    private FragmentTransaction ft = fm.beginTransaction();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        double lat = 90.4786;
        double lon = 23.81435;

        try{
            lat = getIntent().getDoubleExtra("event_lattitude",0.0);
            lon = getIntent().getDoubleExtra("event_longitude",0.0);
        }catch (Exception e){
            Log.e("weatherActivity","No intent data found");
        }



        Bundle bundle= new Bundle();
        bundle.putDouble("lattitude",lat);
        bundle.putDouble("longitude",lon);
        WeatherHomeFragment weatherHomeFragment = new WeatherHomeFragment();
        weatherHomeFragment.setArguments(bundle);
        ft.add(R.id.weather_fragment_coontainer,weatherHomeFragment);
        ft.commit();
    }

    public void showWeatherDetails(View view) {
    }
}
