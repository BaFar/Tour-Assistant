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
import com.example.dell.tourassistant.ConnectivityReceiver;
import com.example.dell.tourassistant.ExtraHelper;
import com.example.dell.tourassistant.R;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;


public class WeatherHomeFragment extends Fragment {


    public WeatherHomeFragment() {
        // Required empty public constructor
    }

    private TextView currentTempTV, descriptionTV,cityNameTV,dateTimeTV;
    private EditText placeSelectorET;
    private ImageView  weatherTypeIV;

    String  cityName,iconCode, dateTime, placeName, description;
    private CurrentWeather weather;
    private Double temp, lat, lon;
       private OnPlacePickListener placePickListener;
    final int PLACE_PICKER_REQUEST_CODE = 1;
    private SharedPreferences preferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_weather_home, container, false);
         placeSelectorET = (EditText) view.findViewById(R.id.place_selector);


        //lat = 90.4786;
        //lon = 23.81435;

        preferences = getActivity().getSharedPreferences("latlonSP", Context.MODE_PRIVATE);

        currentTempTV = (TextView) view.findViewById(R.id.show_current_temp);
        weatherTypeIV = (ImageView) view.findViewById(R.id.show_current_weather_type_icon);
        descriptionTV = (TextView) view.findViewById(R.id.current_weather_description);
        cityNameTV = (TextView) view.findViewById(R.id.city_name);
        dateTimeTV = (TextView) view.findViewById(R.id.date_time);


        Bundle resultBundle = getArguments();

        dateTime = resultBundle.getString("dateTime");
        cityName = resultBundle.getString("city_name");
        temp = resultBundle.getDouble("current_temp");
        iconCode = resultBundle.getString("icon_code");
        description = resultBundle.getString("description");


        // weather = new CurrentWeather();
        placeSelectorET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!ConnectivityReceiver.isConnected()){
                    Toast.makeText(getActivity(), "Opps! No internet Connection. First check internet connection, please!", Toast.LENGTH_LONG).show();
                    return;
                }
                selectPlace();

            }
        });

        return view;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        int weatherIconCode = ExtraHelper.getIconId(iconCode);

        String dayName = ExtraHelper.getDayName(dateTime);
        String dayHour = ExtraHelper.getHour(dateTime);
        cityNameTV.setText(cityName);
        dateTimeTV.setText(dayName+" "+dayHour);
        currentTempTV.setText(String.valueOf(temp) + (char) 0x00B0+"C");/* add dynamic temperature unit later*/
        descriptionTV.setText(description);
        weatherTypeIV.setImageResource(weatherIconCode);
    }


    public void selectPlace() {


        try {
            new PlacePicker.IntentBuilder().build(getActivity());

            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

            startActivityForResult(builder.build(getActivity()), PLACE_PICKER_REQUEST_CODE);
        } catch (GooglePlayServicesRepairableException e) {
            // TODO: Handle the error.
            Log.d("exception","repairable "+e.getMessage());
        } catch (GooglePlayServicesNotAvailableException e) {
            // TODO: Handle the error.
            Log.d("exception","repairable "+e.getMessage());
        }

    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        String PLACE_SELECT_RESULT_TAG ="on place result back";
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
                Log.i(PLACE_SELECT_RESULT_TAG, status.getStatusMessage());


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