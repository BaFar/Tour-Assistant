package com.example.dell.tourassistant.CombinedWeather;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.tourassistant.ExtraHelper;
import com.example.dell.tourassistant.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailsFragment extends Fragment {


    public DetailsFragment() {
        // Required empty public constructor
    }

    private TextView headerTV,feelTV,humidityTV,visibilityTV,uvIndexTV,windTV,pressureTV,sunriseTV,sunsetTV;
    private ImageView detailsIV,windIV,sunMoonIV;
    private String feel_like,humidity,visibility,uv_index,wind,pressure,sunrise,sunset,weather_icon_code;
    int weather_icon_id;
    private double temp;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=  inflater.inflate(R.layout.fragment_details, container, false);
        headerTV = (TextView) v.findViewById(R.id.details_header_text);
        feelTV = (TextView) v.findViewById(R.id.show_feels_temp);;
        humidityTV = (TextView) v.findViewById(R.id.show_humidity);
        visibilityTV = (TextView) v.findViewById(R.id.show_visibility);
        uvIndexTV = (TextView) v.findViewById(R.id.show_uv_index);
        windTV = (TextView) v.findViewById(R.id.show_wind);
        pressureTV = (TextView) v.findViewById(R.id.show_pressure);
        sunriseTV = (TextView) v.findViewById(R.id.show_sunrise);
        sunsetTV = (TextView) v.findViewById(R.id.show_senset);
        detailsIV = (ImageView) v.findViewById(R.id.details_icon);
        windIV = (ImageView) v.findViewById(R.id.wind_pressure_icon);
        sunMoonIV = (ImageView) v.findViewById(R.id.sun_icon);

        Bundle bundle = getArguments();
        feel_like = String.valueOf(bundle.getDouble(WeatherActivity.KEY_FEELS_LIKE));
        humidity = String.valueOf(bundle.getDouble(WeatherActivity.KEY_HUMIDITY));
        visibility = String.valueOf(bundle.getDouble(WeatherActivity.KEY_VISIBILITY));
        uv_index =String.valueOf( bundle.getDouble(WeatherActivity.KEY_UV_INDEX));
        wind = String.valueOf(bundle.getDouble(WeatherActivity.KEY_WIND));
        pressure =String.valueOf( bundle.getDouble(WeatherActivity.KEY_PRESSURE));
        sunrise = bundle.getString(WeatherActivity.KEY_SUNRISE);
        sunset = bundle.getString(WeatherActivity.KEY_SUNSET);
        weather_icon_code = bundle.getString(WeatherActivity.KEY_WEATHER_ICON);


        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SimpleDateFormat parser = new SimpleDateFormat("HH:mm");
        parser.setTimeZone(TimeZone.getTimeZone("UTC"));
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm a");
        formatter.setTimeZone(TimeZone.getTimeZone(WeatherActivity.TIME_ZONE_ID));

        Date parsedRise = null;
        Date parsedSet = null;
        try {
            parsedRise = parser.parse(sunrise);
            parsedSet = parser.parse(sunset);
        } catch (ParseException e) {
            e.printStackTrace();
            Log.d("time_format_error", e.getMessage());
        }
        sunrise = formatter.format(parsedRise);
        sunset = formatter.format(parsedSet);

        weather_icon_id = ExtraHelper.getIconId(weather_icon_code);

       feelTV.setText(feel_like+(char)0x00B0+"C");
       humidityTV.setText(humidity+"%");
       visibilityTV.setText(visibility+"km");
       uvIndexTV.setText(uv_index);
       windTV.setText(wind+"m/s");
       pressureTV.setText(pressure+"mb");
       sunriseTV.setText(sunrise);
       sunsetTV.setText(sunset);

       detailsIV.setImageResource(weather_icon_id);
       windIV.setImageResource(R.drawable.wind_icon);
       sunMoonIV.setImageResource(R.drawable.sunset);


    }
}
