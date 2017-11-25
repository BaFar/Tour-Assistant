package com.example.dell.tourassistant.CombinedWeather;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.tourassistant.CombinedWeather.CurrentWeatherPackage.CurrentWeather;
import com.example.dell.tourassistant.CombinedWeather.CurrentWeatherPackage.CurrentWeatherClient;
import com.example.dell.tourassistant.CombinedWeather.CurrentWeatherPackage.Datum;
import com.example.dell.tourassistant.ExtraHelper;
import com.example.dell.tourassistant.R;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;


public class WeatherHomeFragment extends Fragment {


    public WeatherHomeFragment() {
        // Required empty public constructor
    }

    private TextView currentTempTV, weatherTypeTV, sunsetTV, sunriseTV, cityNameTV, dateTimeTV;
    private EditText placeSelectorET;
    private ImageView minTempIV, maxTempIV, weatherTypeIV;

    String lowTemp, highTemp, weatherCode, cityName, chill, speed, humidity, rising, pressure, sunrise, sunset,
            weatherText, pubdate, weatherType, iconCode, dateTime, placeName;
    private CurrentWeather weather;
    private Double windspeed, visibility, temp, lat, lon;
       private OnPlacePickListener placePickListener;
    final int PLACE_PICKER_REQUEST_CODE = 1;
    private SharedPreferences preferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_weather_home, container, false);
         placeSelectorET = (EditText) view.findViewById(R.id.place_selector);


        lat = 90.4786;
        lon = 23.81435;

        preferences = getActivity().getSharedPreferences("latlonSP", MODE_PRIVATE);


        currentTempTV = (TextView) view.findViewById(R.id.show_current_temp);
        sunsetTV = (TextView) view.findViewById(R.id.show_sunset);
        sunriseTV = (TextView) view.findViewById(R.id.show_sunrise);
        cityNameTV = (TextView) view.findViewById(R.id.show_place_name);
        dateTimeTV = (TextView) view.findViewById(R.id.show_date_time);
        weatherTypeTV = (TextView) view.findViewById(R.id.show_current_weather_type_text);
        weatherTypeIV = (ImageView) view.findViewById(R.id.show_current_weather_type_icon);

        Bundle resultBundle = getArguments();

        dateTime = resultBundle.getString("dateTime");
        cityName = resultBundle.getString("city_name");
        temp = resultBundle.getDouble("current_temp");
        sunrise = resultBundle.getString("sunrise");
        sunset = resultBundle.getString("sunset");
        windspeed = resultBundle.getDouble("windspeed");
        visibility = resultBundle.getDouble("visibility");
        iconCode = resultBundle.getString("icon_code");
        weatherType = resultBundle.getString("description");

        setDatatoView();


        // weather = new CurrentWeather();
        placeSelectorET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectPlace();

            }
        });

        return view;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }


    private void setDatatoView() {


        int weatherIconCode = ExtraHelper.getIconId(iconCode);

        cityNameTV.setText(cityName);
        dateTimeTV.setText(dateTime);
        sunriseTV.setText(sunrise);
        sunsetTV.setText(sunset);
        currentTempTV.setText(String.valueOf(temp) + (char) 0x00B0);/* add unit of temperature later*/
        weatherTypeTV.setText(weatherType);
        weatherTypeIV.setImageResource(weatherIconCode);


    }
    public void selectPlace() {


        try {
            new PlacePicker.IntentBuilder().build(getActivity());

            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

            startActivityForResult(builder.build(getActivity()), PLACE_PICKER_REQUEST_CODE);
        } catch (GooglePlayServicesRepairableException e) {
            // TODO: Handle the error.
            Log.d("exception","repairable");
        } catch (GooglePlayServicesNotAvailableException e) {
            // TODO: Handle the error.
            Log.d("exception","repairable");
        }

    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        String TAG ="on result back";
        if (requestCode == PLACE_PICKER_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Place place = PlacePicker.getPlace(getActivity(), data);
                placeName = String.valueOf(place.getName());
                placeSelectorET.setText(placeName);
                LatLng loc= place.getLatLng();
                lat = loc.latitude;
                lon = loc.longitude;
                SharedPreferences.Editor editor = preferences.edit();
                editor.putFloat("req_lat", (float) loc.latitude);
                editor.putFloat("req_lon", (float) loc.longitude);
                editor.apply();
                editor.commit();
                //collectCurrentWeather(lat,lon);
                placePickListener.onPlacePick(lat,lon);


            } else if (resultCode == PlacePicker.RESULT_ERROR) {
                Status status = PlacePicker.getStatus(getActivity(), data);
                // TODO: Handle the error.
                Log.i(TAG, status.getStatusMessage());

            } else if (resultCode == Activity.RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        placePickListener = (OnPlacePickListener) getActivity();
    }

    public interface OnPlacePickListener{
        void onPlacePick(double lattitude, double longitude);
    }






}